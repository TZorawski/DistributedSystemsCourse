package model;

public class Curso {

    private int codigo;
    private String nome;
    
    public Curso (){
    }
    
    public Curso (int  codigo, String nome){
        this.codigo = codigo;
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return codigo + "-" + nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
}
