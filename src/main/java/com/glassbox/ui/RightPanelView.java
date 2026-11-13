package com.glassbox.ui;

import com.glassbox.chess.domain.*;
import com.glassbox.chess.piece.Piece;
import com.glassbox.uml.event.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.lang.reflect.*;
import java.util.*;

/** Live UML inspector. Every diagram is generated as UML notation and rendered by PlantUML. */
public final class RightPanelView {
    private final VBox pane = new VBox(10);
    private final Label turn = new Label("WHITE TO MOVE"), material = new Label("MATERIAL · 0"), status = new Label("ACTIVE");
    private final ListView<String> moves = new ListView<>();
    private final UmlDiagramView structureDiagram = new UmlDiagramView();
    private final UmlDiagramView classDiagram = new UmlDiagramView();
    private final UmlDiagramView sequenceDiagram = new UmlDiagramView();
    private final UmlDiagramView collaborationDiagram = new UmlDiagramView();
    private final UmlDiagramView componentDiagram = new UmlDiagramView();
    private final List<DomainEvents.RuntimeMessage> messages = new ArrayList<>();
    private final IdentityHashMap<Piece,String> ids = new IdentityHashMap<>();
    private long nextId = 1;
    private String lastActor = "Player", lastPieceType = "Piece", selectedType = "";

    public RightPanelView() {
        pane.setPadding(new Insets(12)); pane.setPrefWidth(510); pane.setMinWidth(300);
        pane.getStyleClass().add("inspector");
        Label heading = new Label("INSPECTOR"); heading.getStyleClass().add("eyebrow");
        Label sub = new Label("LIVE UML  /  RUNTIME MODEL"); sub.getStyleClass().add("panel-subtitle");
        VBox gameBox = new VBox(9, turn, status, material, new Separator(), section("MOVE HISTORY"), moves);
        gameBox.setPadding(new Insets(12)); gameBox.getStyleClass().add("surface"); VBox.setVgrow(moves, Priority.ALWAYS);
        TabPane tabs = new TabPane(); tabs.getStyleClass().add("inspection-tabs");
        addTab(tabs,"Structure",structureDiagram); addTab(tabs,"Class Diagram",classDiagram); addTab(tabs,"Sequence",sequenceDiagram);
        addTab(tabs,"Collaboration",collaborationDiagram); addTab(tabs,"Component",componentDiagram); addTab(tabs,"Game",gameBox);
        tabs.getSelectionModel().select(0); VBox.setVgrow(tabs,Priority.ALWAYS);
        pane.getChildren().addAll(heading,sub,tabs);
        EventBus.getInstance().subscribe(DomainEvents.RuntimeMessage.class, e -> {
            Runnable update = () -> {
                lastActor=e.actor; if(e.pieceType!=null&&!e.pieceType.isBlank())lastPieceType=e.pieceType;
                messages.add(e); if(messages.size()>100)messages.remove(0);
                refreshBehaviorDiagrams();
            };
            Platform.runLater(update);
        });
        setComponentDiagram(); refreshBehaviorDiagrams();
    }

    private static Label section(String s){Label l=new Label(s);l.getStyleClass().add("eyebrow");return l;}
    private static void addTab(TabPane p,String name,javafx.scene.Node n){Tab t=new Tab(name,n);t.setClosable(false);p.getTabs().add(t);}
    public VBox getPane(){return pane;}

    public void update(Board board, Color side, List<String> history, int balance, GameStatus gameStatus, Move last, Position selected) {
        turn.setText(side==Color.WHITE?"WHITE TO MOVE":"BLACK TO MOVE");
        status.setText(gameStatus.name().replace('_',' '));
        status.getStyleClass().remove("state-active"); status.getStyleClass().remove("state-alert");
        status.getStyleClass().add(gameStatus==GameStatus.CHECK||gameStatus==GameStatus.CHECKMATE?"state-alert":"state-active");
        material.setText("MATERIAL  ·  "+(balance>0?"+":"")+balance);
        moves.getItems().setAll(history); if(!history.isEmpty())moves.scrollTo(history.size()-1);
        Piece focused=selected==null?null:board.getPieceAt(selected);
        selectedType=focused==null?"":focused.getClass().getSimpleName();
        structureDiagram.setSource(structureSource(board,last,selected));
        classDiagram.setSource(classDiagramSource());
        refreshBehaviorDiagrams();
    }

    private String id(Piece p){return ids.computeIfAbsent(p,k->"piece"+String.format("%03d",nextId++));}
    private static String safe(String s){return s.replaceAll("[^A-Za-z0-9_]","_");}
    private static String q(String s){return s.replace("\\\\","\\\\\\\\").replace("\\\"","'").replace("\\n"," ");}
    /** Preserve PlantUML's \\n label separator while escaping embedded quotes. */
    private static String umlLabel(String s){return s.replace("\"","'").replace("\r"," ").replace("\n"," ");}
    private static String simple(Class<?> c){return c.isArray()?simple(c.getComponentType())+"[]":c.getSimpleName();}
    private static String vis(int m){return Modifier.isPublic(m)?"+":Modifier.isProtected(m)?"#":Modifier.isPrivate(m)?"-":"~";}
    private static String params(Method m){StringJoiner j=new StringJoiner(", ");for(Class<?> p:m.getParameterTypes())j.add(simple(p));return j.toString();}

    /**
     * Runtime-only object structure. The static type model is intentionally
     * displayed in its own Class Diagram tab.
     */
    private String structureSource(Board board, Move last, Position selected) {
        StringBuilder s = new StringBuilder("""
            @startuml
            top to bottom direction
            skinparam backgroundColor #111827
            skinparam shadowing false
            skinparam linetype ortho
            skinparam nodesep 30
            skinparam ranksep 42
            skinparam packageStyle rectangle
            skinparam packageBackgroundColor #182638
            skinparam packageBorderColor #526b86
            skinparam packageFontColor #eaf1f8
            skinparam object {
              BackgroundColor #26384c
              BorderColor #80a9ce
              FontColor #f5f8fc
              AttributeFontColor #dce8f5
              BackgroundColor<<Selected>> #542c49
              BorderColor<<Selected>> #f08ac5
              FontColor<<Selected>> #ffffff
            }
            skinparam ArrowColor #a8bfd8
            skinparam ArrowFontColor #eaf1f8
            skinparam defaultFontSize 13
            skinparam objectFontSize 13
            title GlassBox · Live Runtime Object Structure
            """);

        String turn = board.getGameState().turn == null ? "unknown" : board.getGameState().turn.toString();
        s.append("object \"game : ChessGame\\nturn = ").append(q(turn)).append("\" as liveGame\n")
         .append("object \"board : Board\\noccupied = ")
         .append(countPieces(board, Color.WHITE) + countPieces(board, Color.BLACK))
         .append("\\nhalfMoveClock = ").append(board.getGameState().halfMoveClock)
         .append("\" as liveBoard\n")
         .append("liveGame *-- \"1\" liveBoard : current board\n");

        appendPieceGroup(s, board, Color.WHITE, "WHITE PIECES", "whiteSet", selected);
        appendPieceGroup(s, board, Color.BLACK, "BLACK PIECES", "blackSet", selected);

        s.append("liveBoard *-- \"1\" whiteSet : white collection\n")
         .append("liveBoard *-- \"1\" blackSet : black collection\n")
         .append("whiteSet -[hidden]right- blackSet\n")
         .append("legend bottom\n  |= Live board snapshot |= Value |\n")
         .append("  | White pieces | ").append(countPieces(board, Color.WHITE)).append(" |\n")
         .append("  | Black pieces | ").append(countPieces(board, Color.BLACK)).append(" |\n")
         .append("  | Selected type | ").append(selectedType.isBlank() ? "none" : selectedType).append(" |\n")
         .append("endlegend\n");
        if (last != null) s.append("note right of liveBoard : last move = ").append(q(last.toString())).append("\n");
        return s.append("@enduml").toString();
    }

    /** A vertical object tree per side; hidden links order siblings top-to-bottom. */
    private void appendPieceGroup(StringBuilder s, Board board, Color color,
                                  String packageName, String alias, Position selected) {
        List<String> objectAliases = new ArrayList<>();
        s.append("package \"").append(packageName).append("\" {\n")
         .append("object \"").append(color == Color.WHITE ? "whitePieces" : "blackPieces")
         .append(" : List<Piece>\\nsize = ").append(countPieces(board, color))
         .append("\" as ").append(alias).append("\n");

        for (int rank = 7; rank >= 0; rank--) {
            for (int file = 0; file < 8; file++) {
                Position pos = new Position(file, rank);
                Piece piece = board.getPieceAt(pos);
                if (piece == null || piece.getColor() != color) continue;
                String oid = id(piece);
                objectAliases.add(oid);
                String label = oid + " : " + piece.getClass().getSimpleName()
                    + "\\nsquare = " + pos.toAlgebraic()
                    + "\\nmoved = " + piece.hasMoved();
                s.append("object \"").append(umlLabel(label)).append("\" as ").append(oid);
                if (pos.equals(selected)) s.append(" <<Selected>>");
                s.append("\n").append(alias).append(" *-- \"1\" ").append(oid).append("\n");
            }
        }
        for (int i = 1; i < objectAliases.size(); i++) {
            s.append(objectAliases.get(i - 1)).append(" -[hidden]down- ")
             .append(objectAliases.get(i)).append("\n");
        }
        s.append("}\n");
    }

    /** Static type hierarchy and multiplicities, separate from runtime objects. */
    private String classDiagramSource() {
        StringBuilder s = new StringBuilder("""
            @startuml
            top to bottom direction
            skinparam backgroundColor #111827
            skinparam shadowing false
            skinparam linetype ortho
            skinparam nodesep 32
            skinparam ranksep 42
            skinparam class {
              BackgroundColor #26384c
              BorderColor #80a9ce
              FontColor #f5f8fc
              AttributeFontColor #dce8f5
              StereotypeFontColor #a9c7e4
              BackgroundColor<<Selected>> #542c49
              BorderColor<<Selected>> #f08ac5
              FontColor<<Selected>> #ffffff
            }
            skinparam ArrowColor #a8bfd8
            skinparam ArrowFontColor #eaf1f8
            skinparam defaultFontSize 14
            skinparam classFontSize 15
            skinparam classAttributeFontSize 13
            title GlassBox · Class Diagram
            """);

        Class<?>[] types = {
            com.glassbox.chess.game.ChessGame.class,
            com.glassbox.chess.domain.Board.class,
            com.glassbox.chess.domain.Square.class,
            com.glassbox.chess.piece.Piece.class,
            com.glassbox.chess.piece.Pawn.class,
            com.glassbox.chess.piece.Rook.class,
            com.glassbox.chess.piece.Knight.class,
            com.glassbox.chess.piece.Bishop.class,
            com.glassbox.chess.piece.Queen.class,
            com.glassbox.chess.piece.King.class,
            com.glassbox.chess.rule.MoveValidator.class,
            com.glassbox.chess.ai.AIStrategy.class,
            com.glassbox.chess.ai.MinimaxAI.class
        };
        for (Class<?> c : types) {
            String name = safe(c.getSimpleName());
            if (c.isInterface()) s.append("interface ").append(name);
            else if (Modifier.isAbstract(c.getModifiers())) s.append("abstract class ").append(name);
            else s.append("class ").append(name);
            if (c.getSimpleName().equals(selectedType)) s.append(" <<Selected>>");
            s.append(" {\n");
            int fieldCount = 0;
            for (Field f : c.getDeclaredFields()) {
                if (f.isSynthetic() || Modifier.isStatic(f.getModifiers()) || Modifier.isPrivate(f.getModifiers())) continue;
                s.append("  ").append(vis(f.getModifiers())).append(f.getName())
                 .append(" : ").append(simple(f.getType())).append("\n");
                if (++fieldCount == 3) break;
            }
            int methodCount = 0;
            for (Method m : c.getDeclaredMethods()) {
                if (m.isSynthetic() || m.getName().startsWith("lambda$") || Modifier.isPrivate(m.getModifiers())) continue;
                s.append("  ").append(vis(m.getModifiers())).append(m.getName()).append("(")
                 .append(params(m)).append(") : ").append(simple(m.getReturnType())).append("\n");
                if (++methodCount == 3) break;
            }
            s.append("}\n");
        }
        return s.append("""
            ChessGame "1" *-- "1" Board : owns
            Board "1" *-- "64" Square : contains
            Board "1" *-- "0..32" Piece : occupies
            Piece <|-- Pawn
            Piece <|-- Rook
            Piece <|-- Knight
            Piece <|-- Bishop
            Piece <|-- Queen
            Piece <|-- King
            MoveValidator ..> Board : validates
            MinimaxAI ..|> AIStrategy
            @enduml
            """).toString();
    }

    private static int countPieces(Board board, Color color) {
        int count = 0;
        for (int rank = 0; rank < 8; rank++)
            for (int file = 0; file < 8; file++) {
                Piece piece = board.getPieceAt(new Position(file, rank));
                if (piece != null && piece.getColor() == color) count++;
            }
        return count;
    }

    private static String shortKey(String k){return k.substring(0,Math.min(12,k.length()));}

    private void refreshBehaviorDiagrams(){
        sequenceDiagram.setSource(sequenceSource());
        collaborationDiagram.setSource(collaborationSource());
    }

    private String sequenceSource(){
        StringBuilder s=new StringBuilder("""
            @startuml
            autonumber
            hide footbox
            skinparam backgroundColor #0b1220
            skinparam shadowing false
            skinparam sequenceArrowColor #7dd3fc
            skinparam sequenceMessageFontColor #f8fafc
            skinparam ArrowFontColor #f8fafc
            skinparam sequenceLifeLineBorderColor #64748b
            skinparam sequenceLifeLineBackgroundColor #1e293b
            skinparam participantBackgroundColor #1e293b
            skinparam participantBorderColor #38bdf8
            skinparam participantFontColor #f8fafc
            skinparam actorBorderColor #60a5fa
            skinparam actorFontColor #f8fafc
            skinparam sequenceMessageAlign center
            """);
        LinkedHashSet<String> endpoints = new LinkedHashSet<>();
        for (DomainEvents.RuntimeMessage m : messages) { endpoints.add(m.from); endpoints.add(m.to); }
        if (endpoints.isEmpty()) { endpoints.add("Player"); endpoints.add("ChessController"); endpoints.add("ChessGame"); endpoints.add("Board"); }
        if (endpoints.contains("Player")) s.append("actor Player\n");
        if (endpoints.contains("AI Agent")) s.append("actor \"AI Agent\" as AIAgent\n");
        for (String endpoint : endpoints) {
            if (endpoint.equals("Player") || endpoint.equals("AI Agent")) continue;
            String alias = participantAlias(endpoint);
            s.append("participant \"").append(q(endpoint)).append("\" as ").append(alias).append("\n");
        }
        if (messages.isEmpty()) {
            s.append("Player -> ChessController : select(piece)\nChessController -> ChessGame : inspectPosition()\nChessGame -> Board : getPieceAt()\n");
        } else {
            int from = Math.max(0, messages.size()-28);
            for (int i=from;i<messages.size();i++) {
                DomainEvents.RuntimeMessage m=messages.get(i);
                String source=participantAlias(m.from), target=participantAlias(m.to);
                s.append(source).append(" -> ").append(target).append(" : ").append(q(m.method)).append("\n");
                if(m.detail!=null&&!m.detail.isBlank()) s.append("note over ").append(target).append(" : ").append(q(m.detail)).append("\n");
            }
        }
        return s.append("@enduml").toString();
    }

    private static String participantAlias(String raw) {
        if (raw == null || raw.isBlank()) return "Unknown";
        if (raw.equals("AI Agent")) return "AIAgent";
        return safe(raw);
    }

    /** Collaboration diagram is derived from the actual recent runtime message edges. */
    private String collaborationSource(){
        // Aggregate repeated calls between the same objects to keep the
        // communication diagram readable instead of drawing a dense edge for
        // every low-level event.
        StringBuilder s = new StringBuilder("""
            @startuml
            left to right direction
            skinparam backgroundColor #111827
            skinparam shadowing false
            skinparam linetype ortho
            skinparam nodesep 85
            skinparam ranksep 75
            skinparam defaultFontSize 13
            skinparam objectFontSize 13
            skinparam ArrowFontSize 13
            skinparam ArrowColor #a8bfd8
            skinparam ArrowFontColor #f8fafc
            skinparam objectAttributeFontColor #dce8f5
            skinparam objectBackgroundColor #26384c
            skinparam objectBorderColor #7fb4d8
            skinparam objectFontColor #f5f8fc
            skinparam wrapWidth 180
            title Runtime Collaboration · Recent Interactions
            """);

        LinkedHashMap<String, LinkedHashSet<String>> links = new LinkedHashMap<>();
        LinkedHashSet<String> nodes = new LinkedHashSet<>();
        int from = Math.max(0, messages.size() - 8);
        for (int i = from; i < messages.size(); i++) {
            DomainEvents.RuntimeMessage m = messages.get(i);
            String source = participantAlias(m.from);
            String target = participantAlias(m.to);
            nodes.add(source);
            nodes.add(target);
            String key = source + "->" + target;
            links.computeIfAbsent(key, k -> new LinkedHashSet<>())
                 .add(m.method == null ? "interaction" : m.method);
        }
        if (nodes.isEmpty()) {
            nodes.addAll(List.of("Player", "ChessController", "ChessGame", "Board"));
            links.put("Player->ChessController", new LinkedHashSet<>(List.of("selectPiece()")));
            links.put("ChessController->ChessGame", new LinkedHashSet<>(List.of("requestMove()")));
            links.put("ChessGame->Board", new LinkedHashSet<>(List.of("applyMove()")));
        }

        for (String node : nodes) {
            String label = node.equals("AIAgent") ? "AI Agent" : node;
            s.append("object \"").append(q(label)).append("\" as ").append(node).append("\n");
        }

        int number = 1;
        for (Map.Entry<String, LinkedHashSet<String>> entry : links.entrySet()) {
            String[] endpoints = entry.getKey().split("->", 2);
            List<String> methods = new ArrayList<>(entry.getValue());
            if (methods.size() > 2) methods = methods.subList(methods.size() - 2, methods.size());
            String label = number++ + ": " + String.join("\\n", methods);
            if (label.length() > 54) label = label.substring(0, 51) + "...";
            s.append(endpoints[0]).append(" --> ").append(endpoints[1])
             .append(" : ").append(q(label)).append("\n");
        }
        s.append("note bottom : Latest runtime interactions · actor = ")
         .append(q(lastActor)).append(" · piece = ").append(q(lastPieceType)).append("\n@enduml");
        return s.toString();
    }

    private void setComponentDiagram(){
        componentDiagram.setSource("@startuml\nleft to right direction\nskinparam backgroundColor #0b1220\nskinparam shadowing false\nskinparam componentStyle uml2\nskinparam componentBackgroundColor #172438\nskinparam componentBorderColor #42c8d5\nskinparam componentFontColor #e5edf7\nskinparam ArrowColor #8ba4c2\ncomponent [JavaFX UI\\nChessBoardView + Controller] as UI\ncomponent [Chess Domain\\nChessGame + Board] as CORE\ncomponent [Piece Rules\\nPiece hierarchy + Validator] as RULES\ncomponent [AI Opponent\\nAIStrategy + Minimax] as AI\ncomponent [Runtime Inspector\\nEventBus + UML Renderer] as INSPECTOR\ninterface \"IGame Operations\" as GAME_API\ninterface \"IAIStrategy\" as AI_API\nUI - GAME_API\nGAME_API -- CORE\nCORE --> RULES : polymorphic dispatch\nCORE - AI_API\nAI_API -- AI\nCORE ..> INSPECTOR : runtime domain events\\n(read-only)\n@enduml");
    }
}
