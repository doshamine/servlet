package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.config.PostConfig;
import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
  public static final String basePath = "/servlet_war_exploded/api/posts";
  public static final String GET = "GET";
  public static final String POST = "POST";
  public static final String DELETE = "DELETE";
  private PostController controller;

  @Override
  public void init() {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(PostConfig.class);
    this.controller = context.getBean(PostController.class);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();

      if (method.equals(GET) && path.equals(basePath)) {
        doGet(req, resp);
        return;
      }
      if (method.equals(GET) && path.matches(basePath + "/\\d+")) {
        doGet(req, resp);
        return;
      }
      if (method.equals(POST) && path.equals(basePath)) {
        doPost(req, resp);
        return;
      }
      if (method.equals(DELETE) && path.matches(basePath + "/\\d+")) {
        doDelete(req, resp);
        return;
      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String path = req.getRequestURI();
    if (path.matches(basePath + "/\\d+")) {
      final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
      controller.getById(id, resp);
      return;
    }
    controller.all(resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    controller.save(req.getReader(), resp);
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String path = req.getRequestURI();
    final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
    controller.removeById(id, resp);
  }
}

