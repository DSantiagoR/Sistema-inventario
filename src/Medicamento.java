public class Medicamento {
    private final String codigo;
    private final String nombre;
    private final int umbralMinimo;
    private final int diasAlertaVencimiento;

    public Medicamento(String codigo, String nombre, int umbralMinimo, int diasAlertaVencimiento) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.umbralMinimo = umbralMinimo;
        this.diasAlertaVencimiento = diasAlertaVencimiento;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public int getUmbralMinimo() { return umbralMinimo; }
    public int getDiasAlertaVencimiento() { return diasAlertaVencimiento; }
}
