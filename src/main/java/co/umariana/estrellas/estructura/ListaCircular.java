package co.umariana.estrellas.estructura;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Lista enlazada CIRCULAR del equipo (tarea de ESTRUCTURA).
 *
 * <p>El último nodo apunta de nuevo al primero. Implemente la lista con sus propios nodos: no se
 * permite usar {@code ArrayList}, {@code LinkedList} ni ninguna otra colección de {@code java.util}
 * para guardar los elementos (las pruebas lo revisan).
 *
 * <p>Comportamiento especial por ser circular: el cursor NUNCA se detiene. {@link #avanzar()} desde
 * el último elemento vuelve al primero, y {@link #retroceder()} desde el primero va al último.
 * Ambos devuelven {@code true} mientras la lista no esté vacía.
 *
 * <p>El iterador recorre los elementos UNA sola vez, desde el primero hasta el último (¡sin quedar
 * dando vueltas para siempre!).
 */
public class ListaCircular<T> implements Lista<T> {

    private class Nodo {
        private T elemento;
        private Nodo siguiente;
        private Nodo anterior;

        private Nodo(T elemento) {
            this.elemento = elemento;
        }
    }

    private Nodo cabeza;
    private Nodo cursor;
    private int tamano;

    @Override
    public void agregar(T elemento) {
        insertar(tamano, elemento);
    }

    @Override
    public void insertar(int indice, T elemento) {
        if (indice < 0 || indice > tamano) {
            throw new IndexOutOfBoundsException("Indice: " + indice + ", tamano: " + tamano);
        }

        Nodo nuevo = new Nodo(elemento);
        if (tamano == 0) {
            nuevo.siguiente = nuevo;
            nuevo.anterior = nuevo;
            cabeza = nuevo;
            cursor = nuevo;
        } else {
            Nodo siguiente = indice == tamano ? cabeza : nodoEn(indice);
            Nodo anterior = siguiente.anterior;
            nuevo.anterior = anterior;
            nuevo.siguiente = siguiente;
            anterior.siguiente = nuevo;
            siguiente.anterior = nuevo;
            if (indice == 0) {
                cabeza = nuevo;
            }
        }
        tamano++;
    }

    @Override
    public T obtener(int indice) {
        validarIndice(indice);
        return nodoEn(indice).elemento;
    }

    @Override
    public T eliminar(int indice) {
        validarIndice(indice);
        Nodo eliminado = nodoEn(indice);
        T elemento = eliminado.elemento;

        if (tamano == 1) {
            cabeza = null;
            cursor = null;
        } else {
            eliminado.anterior.siguiente = eliminado.siguiente;
            eliminado.siguiente.anterior = eliminado.anterior;
            if (eliminado == cabeza) {
                cabeza = eliminado.siguiente;
            }
            if (eliminado == cursor) {
                cursor = eliminado.siguiente;
            }
        }

        tamano--;
        eliminado.siguiente = null;
        eliminado.anterior = null;
        return elemento;
    }

    @Override
    public int tamano() {
        return tamano;
    }

    @Override
    public boolean estaVacia() {
        return tamano == 0;
    }

    @Override
    public void vaciar() {
        cabeza = null;
        cursor = null;
        tamano = 0;
    }

    @Override
    public T actual() {
        return cursor == null ? null : cursor.elemento;
    }

    @Override
    public boolean avanzar() {
        if (estaVacia()) {
            return false;
        }
        cursor = cursor.siguiente;
        return true;
    }

    @Override
    public boolean retroceder() {
        if (estaVacia()) {
            return false;
        }
        cursor = cursor.anterior;
        return true;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private Nodo siguiente = cabeza;
            private int restantes = tamano;

            @Override
            public boolean hasNext() {
                return restantes > 0;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T elemento = siguiente.elemento;
                siguiente = siguiente.siguiente;
                restantes--;
                return elemento;
            }
        };
    }

    private Nodo nodoEn(int indice) {
        Nodo nodo;
        if (indice < tamano / 2) {
            nodo = cabeza;
            for (int i = 0; i < indice; i++) {
                nodo = nodo.siguiente;
            }
        } else {
            nodo = cabeza.anterior;
            for (int i = tamano - 1; i > indice; i--) {
                nodo = nodo.anterior;
            }
        }
        return nodo;
    }

    private void validarIndice(int indice) {
        if (indice < 0 || indice >= tamano) {
            throw new IndexOutOfBoundsException("Indice: " + indice + ", tamano: " + tamano);
        }
    }
}
