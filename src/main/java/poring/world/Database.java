package poring.world;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import poring.model.Author;

import java.io.Serializable;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class Database {

  // Database
  public static final String DATABASE_URL = System.getenv("JDBC_DATABASE_URL");

  private SessionFactory sessionFactory;

  public SessionFactory getDBConnection() {
    if (sessionFactory == null) {
      if (DATABASE_URL == null) {
        System.out.println("Invalid jdbc URI to connect, exiting...");
        return null;
      }

      Map<String,String> jdbcUrlSettings = new HashMap<>();
      URI uri = URI.create(DATABASE_URL);
      String[] userInfo = uri.getUserInfo().split(":");
      String hibernateUrl = String.format("jdbc:postgresql://%s:5432%s?sslmode=require", uri.getHost(), uri.getPath());
      jdbcUrlSettings.put("hibernate.connection.url", hibernateUrl);
      jdbcUrlSettings.put("hibernate.connection.username", userInfo[0]);
      jdbcUrlSettings.put("hibernate.connection.password", userInfo[1]);

      StandardServiceRegistry standardRegistry
          = new StandardServiceRegistryBuilder()
          .configure()
          .applySettings(jdbcUrlSettings)
          .build();

      MetadataSources sources = new MetadataSources(standardRegistry);
      sources.addAnnotatedClass(Author.class );
      Metadata metadata = sources.buildMetadata();

      this.sessionFactory = metadata.buildSessionFactory();
    }

    return sessionFactory;
  }

  public Author findAuthorByDiscordId(String discordId) {
    Session s = this.getDBConnection().openSession();
    CriteriaBuilder builder = s.getCriteriaBuilder();
    CriteriaQuery<Author> query = builder.createQuery(Author.class);
    Root<Author> root = query.from(Author.class);
    query.select(root).where(builder.equal(root.get("discord_id"), discordId));

    Author author = s.createQuery(query).getSingleResult();
    s.close();
    return author;
  }

  public void save(Serializable obj) {
    Session s = this.getDBConnection().openSession();
    Transaction tx = s.beginTransaction();
    s.saveOrUpdate(obj);
    tx.commit();
    s.close();
  }

  public void close() {
    if (this.sessionFactory != null) {
      this.sessionFactory.close();
    }
  }

}
