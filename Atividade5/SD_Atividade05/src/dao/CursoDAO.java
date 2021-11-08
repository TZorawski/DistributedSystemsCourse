package dao;

import model.Curso;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author André  Schwerz
 */
public class CursoDAO extends DbConnection{
    private Connection conn;
    private final String sqlInsert = "INSERT INTO Curso (codigo, nome) VALUES (?,?)";
    private final String sqlUpdate = "UPDATE Curso SET nome = ? WHERE codigo = ? ";
    private final String sqlRemove = "DELETE FROM Curso WHERE codigo = ?";
    private final String sqlList   = "SELECT codigo, nome FROM Curso ORDER BY nome";
    private final String sqlFind   = "SELECT codigo, nome FROM Curso WHERE codigo = ?";

    public void insert(Curso curso) throws SQLException{
        System.out.println("Iniciando");
        conn = connect();
        System.out.println("Conectado");
        PreparedStatement ps = null;
        try{
            System.out.println("Utilizando con");
            ps = conn.prepareStatement(sqlInsert);
            System.out.println("Construindo query1");
            ps.setInt(1, curso.getCodigo());
            System.out.println("Construindo query2");
            ps.setString(2, curso.getNome());
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
    
    public void update(Curso curso) throws SQLException{
        PreparedStatement ps = null;
        try{
            conn = connect();
            ps = conn.prepareStatement(sqlUpdate);
            ps.setString(1, curso.getNome());
            ps.setInt(2, curso.getCodigo());
            ps.execute();
        }
        finally{
            ps.close();
            close(conn);
        }
    }
    
    public void remove(int codigo) throws SQLException{
        PreparedStatement ps = null;
        try{
            conn = connect();
            ps = conn.prepareStatement(sqlRemove);
            ps.setInt(1, codigo);
            ps.execute();
        }
        finally{
            ps.close();
            close(conn);
        }

    }
    
    public ArrayList<Curso> list() throws SQLException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            conn = connect();
            ps = conn.prepareStatement(sqlList);
            rs = ps.executeQuery();
            ArrayList<Curso> list = new ArrayList<>();
            Curso curso;
            while (rs.next()){
                curso = new Curso();
                curso.setCodigo(rs.getInt("codigo"));
                curso.setNome(rs.getString("nome"));
                list.add(curso);
            }
            return list;
        }
        finally{
            rs.close();
            ps.close();
            close(conn);
        }
    }

    public Curso find(int codigo)throws SQLException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            conn = connect();
            ps = conn.prepareStatement(sqlFind);
            ps.setInt(1, codigo);

            rs = ps.executeQuery();
            Curso curso = null ;

            if (rs.next()){
                curso = new Curso();
                curso.setCodigo(rs.getInt("codigo"));
                curso.setNome(rs.getString("nome"));
            }
            return curso;
        }
        finally{
            rs.close();
            ps.close();
            close(conn);
        }       
    }
}