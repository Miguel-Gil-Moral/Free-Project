const menu = document.querySelector(".menu_cerrado");
const abrirMenu = document.querySelector(".boton_menu");
const cerrarMenu = document.querySelector(".cerrar_menu");
const main = document.getElementsByTagName("main");

abrirMenu.addEventListener("click", function () {

    menu.style.transitionTimingFunction = "ease-out";
    menu.classList.toggle("menu_abierto");
    menu.classList.remove("menu_cerrado");
});

cerrarMenu.addEventListener("click", function () {

    menu.style.transitionTimingFunction = "ease-in";
    menu.classList.toggle("menu_cerrado");
    menu.classList.remove("menu_abierto");
});

const botonDerecha = document.getElementById("boton_izquierda");
const botonIzquierda = document.getElementById("boton_derecha");

let numeroImagen = 1;

const imagen = document.getElementById("capturaJuego");

botonDerecha.addEventListener("click", function () {
    if (numeroImagen == 3) {
        numeroImagen = 1;
    } else {
        numeroImagen++;
    }
    
    imagen.src = "./imagenes/Captura" + numeroImagen + ".PNG";
});

botonIzquierda.addEventListener("click", function () {
    if (numeroImagen == 1) {
        numeroImagen = 3;
    } else {
        numeroImagen--;
    }
    
    imagen.src = "./imagenes/Captura" + numeroImagen + ".PNG";
});