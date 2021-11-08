/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author thais
 */
public class Disciplina {

    private String codigo;
    private String nome;
    private String professor;
    private int cod_curso;

    public Disciplina() {
        //
    }
    
    public Disciplina(String codigo, String nome, String professor, int cod_curso) {
        this.codigo = codigo;
        this.nome = nome;
        this.professor = professor;
        this.cod_curso = cod_curso;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public int getCod_curso() {
        return cod_curso;
    }

    public void setCod_curso(int cod_curso) {
        this.cod_curso = cod_curso;
    }

    @Override
    public String toString() {
        return "Disciplina{" + "codigo=" + codigo + ", nome=" + nome + ", professor=" + professor + ", cod_curso=" + cod_curso + '}';
    }
    
    
}
