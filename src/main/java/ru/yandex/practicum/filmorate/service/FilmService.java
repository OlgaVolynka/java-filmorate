package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FilmService {
    //private final InMemoryFilmStorage inMemoryFilmStorage;
    private final UserDbStorage userDbStorage;
    private static final LocalDate MIN_DATA = LocalDate.of(1895, 12, 28);
    private final JdbcTemplate jdbcTemplate;
    private final FilmDbStorage filmDbStorage;


    @Autowired
    public FilmService(FilmDbStorage filmDbStorage, JdbcTemplate jdbcTemplate, UserDbStorage userDbStorage) {
        this.filmDbStorage = filmDbStorage;
        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = userDbStorage;
    }

//    @Autowired
 //   public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
 //       this.inMemoryFilmStorage = inMemoryFilmStorage;
 //   }

   public void addLike(long id, long userId) {

        filmDbStorage.getFilmById(id);

       String sqlQuery = "insert into film_likes(user_id, film_id) " +
               "values (?, ?)";
       jdbcTemplate.update(sqlQuery,
               userId,
               id

       );

        //filmDbStorage.getFilmById(id).addLike(userId);
    }

    public void deleteLike(Long id, Long userId) {

        filmDbStorage.getFilmById(id);
        userDbStorage.getUserById(userId);

     //   if (!inMemoryFilmStorage.getFilms().get(id).getLikes().contains(userId)) {
      //      throw new DataNotFoundException("не найден id пользователя");
      //  }

        String sqlQuery = "delete from film_likes where film_id = ? and user_id = ?";
        jdbcTemplate.update(sqlQuery, id, userId);

        // filmDbStorage.getFilmById(id).removeLike(userId);

    }

    //"select film_id, genre_id, genres_name from films_genres left join genres on films_genres.genre_id = genres.id where film_id = ?";
    public List<Film> getPopular(Integer count) {
        List<Film> filmSet = new ArrayList<>();

       // SqlRowSet rs = jdbcTemplate.queryForRowSet("select id  from films limit ?;", count);

        SqlRowSet rs = jdbcTemplate.queryForRowSet("select films.ID, count(film_likes.USER_ID) as count_user  from films left outer join film_likes on FILMS.ID = FILM_LIKES.FILM_ID group by FILMS.ID order by count_user desc limit ?", count);
        while (rs.next()) {
            filmSet.add(filmDbStorage.getFilmById((long) rs.getInt("id")));
        }

return filmSet;

   /*     List<Film> filmSet = new ArrayList<>(filmDbStorage.findAll());
        return filmSet.stream().sorted((p0, p1) -> {
            int comp = p0.getLikes().size() - (p1.getLikes().size());
            return -1 * comp;
        }).limit(count).collect(Collectors.toList());*/
    }

    public ArrayList<Film> getFilmById(Long id) {

        return new ArrayList<>(Collections.singleton(filmDbStorage.getFilmById(id)));
    }

    public Film createFilm(Film film) {

       if (film.getReleaseDate().isBefore(MIN_DATA)) {
           throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
       }


        return filmDbStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {

        filmDbStorage.getFilmById(film.getId());

        if (film.getReleaseDate().isBefore(MIN_DATA)) {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        } else {
            return filmDbStorage.updateFilm(film);
        }
    }
}
