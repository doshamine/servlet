package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepository {
  private final Map<Long, Post> postMap = new ConcurrentHashMap<>();
  private AtomicLong UID = new AtomicLong(1L);

  public List<Post> all() {
    return postMap.values().stream().toList();
  }

  public Optional<Post> getById(long id) {
    return Optional.ofNullable(postMap.get(id));
  }

  public Optional<Post> save(Post post) {
    long id = post.getId();

    if (id == 0) {
      id = UID.getAndIncrement();
      post.setId(id);
    } else {
      Post oldPost = postMap.get(id);
      if (oldPost == null) {
        return Optional.empty();
      }
    }

    postMap.put(id, post);
    return Optional.of(post);
  }

  public Optional<Post> removeById(long id) {
    return Optional.ofNullable(postMap.remove(id));
  }
}
