package presentacion;

public class ProductoComboItem {
    private Long id;
    private String nombre;

    public ProductoComboItem(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
