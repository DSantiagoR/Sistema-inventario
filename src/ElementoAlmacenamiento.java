import java.util.List;

public interface ElementoAlmacenamiento {

    String getNombre();

    int contarUbicaciones();

    int contarUbicacionesOcupadas();

    int getUnidadesAlmacenadas();

    List<Ubicacion> listarUbicacionesLibres();

    List<Lote> buscarLotesPorVencer(int dias);

    /** Imprime este nodo y, recursivamente, sus hijos, con sangría. */
    void mostrarEstructura(int nivelIndentacion);

    default void agregar(ElementoAlmacenamiento hijo) {
        throw new UnsupportedOperationException(
                "No se pueden agregar elementos dentro de una ubicación (" + getNombre() + ").");
    }

    default void eliminar(ElementoAlmacenamiento hijo) {
        throw new UnsupportedOperationException(
                "No se pueden eliminar elementos de una ubicación (" + getNombre() + ").");
    }
}
