const views = {
  structure: {title:"Runtime structure", index:"VIEW 01", description:"A snapshot of the live game model and its object relationships.", image:"assets/structure.png", alt:"GlassBox runtime structure UML screenshot"},
  class: {title:"Class diagram", index:"VIEW 02", description:"The static type model: classes, interfaces, attributes, methods and inheritance.", image:"assets/class-diagram.png", alt:"GlassBox class diagram screenshot"},
  sequence: {title:"Sequence diagram", index:"VIEW 03", description:"A chronological view of the messages exchanged during a chess interaction.", image:"assets/sequence.png", alt:"GlassBox sequence diagram screenshot"},
  collaboration: {title:"Collaboration diagram", index:"VIEW 04", description:"Objects and numbered messages showing how components work together.", image:"assets/collaboration.png", alt:"GlassBox collaboration diagram screenshot"},
  component: {title:"Component diagram", index:"VIEW 05", description:"A higher-level view of the system's components and their dependencies.", image:"assets/component.png", alt:"GlassBox component diagram screenshot"},
  game: {title:"Game inspector", index:"VIEW 06", description:"Game status, material balance and move history alongside the chessboard.", image:"assets/game.png", alt:"GlassBox game inspector screenshot"}
};
const tabs=[...document.querySelectorAll(".view-tab")];
const viewImage=document.getElementById("viewImage");
function setView(key){
  const v=views[key]; if(!v)return;
  tabs.forEach(t=>{const active=t.dataset.view===key;t.classList.toggle("active",active);t.setAttribute("aria-selected",String(active));});
  viewImage.style.opacity="0";
  window.setTimeout(()=>{viewImage.src=v.image;viewImage.alt=v.alt;viewImage.style.opacity="1";},120);
  document.getElementById("viewTitle").textContent=v.title;
  document.getElementById("viewIndex").textContent=v.index;
  document.getElementById("viewDescription").textContent=v.description;
}
tabs.forEach(t=>t.addEventListener("click",()=>setView(t.dataset.view)));
const lightbox=document.getElementById("lightbox"), lightboxImg=lightbox.querySelector("img");
document.getElementById("imageOpen").addEventListener("click",()=>{lightboxImg.src=viewImage.src;lightboxImg.alt=viewImage.alt;lightbox.classList.add("open");lightbox.setAttribute("aria-hidden","false");});
function closeLightbox(){lightbox.classList.remove("open");lightbox.setAttribute("aria-hidden","true");}
lightbox.addEventListener("click",e=>{if(e.target===lightbox||e.target.classList.contains("lightbox-close"))closeLightbox();});
document.addEventListener("keydown",e=>{if(e.key==="Escape")closeLightbox();});
document.getElementById("copyCommands").addEventListener("click",async e=>{
 const text="git clone https://github.com/rudraminaa/glassbox-engine.git\ncd glassbox-engine\nmvn clean install\nmvn test\nmvn javafx:run";
 try{await navigator.clipboard.writeText(text);e.currentTarget.textContent="COPIED";setTimeout(()=>e.currentTarget.textContent="COPY",1500);}
 catch{e.currentTarget.textContent="SELECT TO COPY";}
});
const menu=document.querySelector(".menu-toggle"),nav=document.querySelector(".nav-links");
menu.addEventListener("click",()=>{const open=nav.classList.toggle("open");menu.setAttribute("aria-expanded",String(open));});
nav.querySelectorAll("a").forEach(a=>a.addEventListener("click",()=>{nav.classList.remove("open");menu.setAttribute("aria-expanded","false");}));
