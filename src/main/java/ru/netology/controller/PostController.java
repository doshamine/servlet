package ru.netology.controller;

import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;

@Controller
public class PostController {
  public static final String APPLICATION_JSON = "application/json";
  private final PostService service;
  private static final Gson gson = new Gson();

  public PostController(PostService postService) {
      this.service = postService;
  }

  public void all(HttpServletResponse response) throws IOException {
    response.setContentType(APPLICATION_JSON);
    final var data = service.all();
    response.getWriter().print(gson.toJson(data));
    response.setStatus(HttpServletResponse.SC_OK);
  }

  public void getById(long id, HttpServletResponse response) throws IOException {
    try {
      final var data = service.getById(id);
      response.getWriter().print(gson.toJson(data));
      response.setStatus(HttpServletResponse.SC_OK);
    } catch (NotFoundException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  public void save(Reader body, HttpServletResponse response) throws IOException {
    response.setContentType(APPLICATION_JSON);
    final var post = gson.fromJson(body, Post.class);

    try {
      final var data = service.save(post);
      response.getWriter().print(gson.toJson(data));

      if (post.getId() != 0) {
        response.setStatus(HttpServletResponse.SC_OK);
      } else {
        response.setStatus(HttpServletResponse.SC_CREATED);
      }
    } catch (NotFoundException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
  }

  public void removeById(long id, HttpServletResponse response) throws IOException {
    try {
      service.removeById(id);
      response.getWriter().print(gson.toJson(id));
      response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    } catch (NotFoundException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }
}
