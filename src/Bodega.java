public class Bodega extends Contenedor {

    private final String idBodega;

    public Bodega(String idBodega, String nombre) {
        super(nombre);
        this.idBodega = idBodega;
    }

    public String getIdBodega() {
        return idBodega;
    }
}
