package dao;

import model.Aluno;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author André  Schwerz
 */
public class AlunoDAO extends DbConnection{
    private Connection conn;
    private final String sqlInsert = "INSERT INTO Aluno (RA, nome, periodo, cod_curso) VALUES (?,?,?,?)";
    private final String sqlUpdate = "UPDATE Aluno SET nome = ?, periodo = ?, cod_curso = ? WHERE RA = ? ";
    private final String sqlRemove = "DELETE FROM Aluno WHERE RA = ?";
    private final String sqlList   = "SELECT RA, nome, periodo, cod_curso FROM Aluno ORDER BY nome";
    private final String sqlFind   = "SELECT RA, nome, periodo, cod_curso FROM Aluno WHERE RA = ?";

    public void insert(Aluno aluno) throws SQLException{
        System.out.println("Iniciando");
        conn = connect();
        System.out.println("Conectado");
        PreparedStatement ps = null;
        try{
            System.out.println("Utilizando con");
            ps = conn.prepareStatement(sqlInsert);
            System.out.println("Construindo query1");
            ps.setInt(1, aluno.getRA());
            System.out.println("Construindo query2");
            ps.setString(2, aluno.getNome());
            ps.setInt(3, aluno.getPeriodo());
            ps.setInt(4, aluno.getCod_curso());
            System.out.println("Executando");
            ps.execute();
            System.out.println("executou");
        }
        finally{
            System.out.println("finalizando");
            ps.close();
            close(conn);
            System.out.println("finalizado");
        }
        
    }
    
    public void update(Aluno aluno) throws SQLException{
        PreparedStatement ps = null;
        try{
            conn = connect();
            ps = conn.prepareStatement(sqlUpdate);
            ps.setString(1, aluno.getNome());
            ps.setInt(2, aluno.getPeriodo());
            ps.setInt(3, aluno.getCod_curso());
            ps.setInt(4, aluno.getRA());
            ps.execute();
        }
        finally{
            ps.close();
            close(conn);
        }
    }
    
    public void remove(int RA) throws SQLException{
        PreparedStatement ps = null;
        try{
            conn = connect();
            ps = conn.prepareStatement(sqlRemove);
            ps.setInt(1, RA);
            ps.execute();
        }
        finally{
            ps.close();
            close(conn);
        }

    }
    
    public ArrayList<Aluno> list() throws SQLException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            conn = connect();
            ps = conn.prepareStatement(sqlList);
            rs = ps.executeQuery();
            ArrayList<Aluno> list = new ArrayList<>();
            Aluno aluno;
            while (rs.next()){
                aluno = new Aluno();
                aluno.setRA(rs.getInt("RA"));
                aluno.setNome(rs.getString("nome"));
                aluno.setPeriodo(rs.getInt("periodo"));
                aluno.setCod_curso(rs.getInt("cod_curso"));
                list.add(aluno);
            }
            return list;
        }
        finally{
            rs.close();
            ps.close();
            close(conn);
        }
    }

    public Aluno find(int RA)throws SQLException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            conn = connect();
            ps = conn.prepareStatement(sqlFind);
            ps.setInt(1, RA);

            rs = ps.executeQuery();
            Aluno aluno = null ;

            if (rs.next()){
                aluno = new Aluno();
                aluno.setRA(rs.getInt("RA"));
                aluno.setNome(rs.getString("nome"));
                aluno.setPeriodo(rs.getInt("periodo"));
                aluno.setCod_curso(rs.getInt("cod_curso"));
            }
            return aluno;
        }
        finally{
            rs.close();
            ps.close();
            close(conn);
        }       
    }
}