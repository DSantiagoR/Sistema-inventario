import java.util.Collections;
import java.util.List;

public class Ubicacion implements ElementoAlmacenamiento {

    private final String estanteria;
    private final String cajon;
    private final String nivel;
    private EstadoUbicacion estado;
    private Lote lote;

    public Ubicacion(String estanteria, String cajon, String nivel) {
        this.estanteria = estanteria;
        this.cajon = cajon;
        this.nivel = nivel;
        this.estado = EstadoUbicacion.DISPONIBLE;
    }

    public void asignarLote(Lote lote) {
        if (estaOcupada()) {
            throw new IllegalStateException("La ubicación " + getNombre()
                    + " ya está ocupada por el lote " + this.lote.getIdLote() + ".");
        }
        this.lote = lote;
        this.estado = EstadoUbicacion.OCUPADA;
    }

    public void liberar() {
        this.lote = null;
        this.estado = EstadoUbicacion.DISPONIBLE;
    }

    public boolean estaOcupada() {
        return estado == EstadoUbicacion.OCUPADA;
    }

    public Lote getLote() { return lote; }
    public EstadoUbicacion getEstado() { return estado; }

    @Override
    public String getNombre() {
        return "Est. " + estanteria + " / Cajón " + cajon + " / Nivel " + nivel;
    }

    @Override
    public int contarUbicaciones() {
        return 1;
    }

    @Override
    public int contarUbicacionesOcupadas() {
        return estaOcupada() ? 1 : 0;
    }

    @Override
    public int getUnidadesAlmacenadas() {
        return lote == null ? 0 : lote.getCantidad();
    }

    @Override
    public List<Ubicacion> listarUbicacionesLibres() {
        return estaOcupada() ? Collections.emptyList() : List.of(this);
    }

    @Override
    public List<Lote> buscarLotesPorVencer(int dias) {
        if (lote != null && lote.calcularDiasParaVencer() <= dias) {
            return List.of(lote);
        }
        return Collections.emptyList();
    }

    @Override
    public void mostrarEstructura(int nivelIndentacion) {
        String sangria = "  ".repeat(nivelIndentacion);
        String contenido = lote == null
                ? "libre"
                : lote.getMedicamento().getNombre() + ", lote " + lote.getIdLote()
                  + ", " + lote.getCantidad() + " unid., vence en " + lote.calcularDiasParaVencer() + " días";
        System.out.printf("%s- %s (%s) -> %s%n", sangria, getNombre(), estado, contenido);
    }
}
