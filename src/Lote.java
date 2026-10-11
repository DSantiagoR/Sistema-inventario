import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Lote {
    private final String idLote;
    private final String numeroLote;
    private final LocalDate fechaIngreso;
    private final LocalDate fechaVencimiento;
    private int cantidad;
    private final Medicamento medicamento;

    public Lote(String idLote, String numeroLote, LocalDate fechaIngreso,
                LocalDate fechaVencimiento, int cantidad, Medicamento medicamento) {
        this.idLote = idLote;
        this.numeroLote = numeroLote;
        this.fechaIngreso = fechaIngreso;
        this.fechaVencimiento = fechaVencimiento;
        this.cantidad = cantidad;
        this.medicamento = medicamento;
    }

    public int calcularDiasParaVencer() {
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), fechaVencimiento);
    }

    public void actualizarCantidad(int cant) {
        if (cant < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa.");
        }
        this.cantidad = cant;
    }

    public String getIdLote() { return idLote; }
    public String getNumeroLote() { return numeroLote; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public int getCantidad() { return cantidad; }
    public Medicamento getMedicamento() { return medicamento; }
}
