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
public class Aluno {
    private int RA;
    private String nome;
    private int periodo;
    private int cod_curso;

    /**
     * @return the RA
     */
    public int getRA() {
        return RA;
    }

    /**
     * @param RA the RA to set
     */
    public void setRA(int RA) {
        this.RA = RA;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the periodo
     */
    public int getPeriodo() {
        return periodo;
    }

    /**
     * @param periodo the periodo to set
     */
    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    /**
     * @return the cod_curso
     */
    public int getCod_curso() {
        return cod_curso;
    }

    /**
     * @param cod_curso the cod_curso to set
     */
    public void setCod_curso(int cod_curso) {
        this.cod_curso = cod_curso;
    }
    
}
