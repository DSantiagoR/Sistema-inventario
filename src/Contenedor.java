import java.util.ArrayList;
import java.util.List;

public abstract class Contenedor implements ElementoAlmacenamiento {

    protected final String nombre;
    protected final List<ElementoAlmacenamiento> hijos = new ArrayList<>();

    protected Contenedor(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public void agregar(ElementoAlmacenamiento hijo) {
        hijos.add(hijo);
    }

    @Override
    public void eliminar(ElementoAlmacenamiento hijo) {
        hijos.remove(hijo);
    }

    @Override
    public int contarUbicaciones() {
        int total = 0;
        for (ElementoAlmacenamiento hijo : hijos) {
            total += hijo.contarUbicaciones();
        }
        return total;
    }

    @Override
    public int contarUbicacionesOcupadas() {
        int total = 0;
        for (ElementoAlmacenamiento hijo : hijos) {
            total += hijo.contarUbicacionesOcupadas();
        }
        return total;
    }

    @Override
    public int getUnidadesAlmacenadas() {
        int total = 0;
        for (ElementoAlmacenamiento hijo : hijos) {
            total += hijo.getUnidadesAlmacenadas();
        }
        return total;
    }

    @Override
    public List<Ubicacion> listarUbicacionesLibres() {
        List<Ubicacion> libres = new ArrayList<>();
        for (ElementoAlmacenamiento hijo : hijos) {
            libres.addAll(hijo.listarUbicacionesLibres());
        }
        return libres;
    }

    @Override
    public List<Lote> buscarLotesPorVencer(int dias) {
        List<Lote> lotes = new ArrayList<>();
        for (ElementoAlmacenamiento hijo : hijos) {
            lotes.addAll(hijo.buscarLotesPorVencer(dias));
        }
        return lotes;
    }

    @Override
    public void mostrarEstructura(int nivelIndentacion) {
        String sangria = "  ".repeat(nivelIndentacion);
        System.out.printf("%s- %s [%d/%d ubicaciones ocupadas, %d unidades]%n",
                sangria, nombre, contarUbicacionesOcupadas(), contarUbicaciones(), getUnidadesAlmacenadas());
        for (ElementoAlmacenamiento hijo : hijos) {
            hijo.mostrarEstructura(nivelIndentacion + 1);
        }
    }
}
