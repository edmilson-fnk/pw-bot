package poring.world;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import poring.model.Author;

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

  public Author findByDiscordId(String discordId) {
    Session s = new Database().getDBConnection().openSession();
    CriteriaBuilder builder = s.getCriteriaBuilder();
    CriteriaQuery<Author> query = builder.createQuery(Author.class);
    Root<Author> root = query.from(Author.class);
    query.select(root).where(builder.equal(root.get("discord_id"), discordId));

    return s.createQuery(query).getSingleResult();
  }

}
