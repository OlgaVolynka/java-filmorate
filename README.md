# java-filmorate
в диаграмме представлены две основные таблицы "Фильмы" (films) "Пользователи"(users).

Пользователи могут ставить лайки на фильмы (причем на фильм могут поставить лайки разные пользователи и один пользователь может поставить лайки на разные фильмыю НО один пользователь на уникальный фильм может поставить только один лайк). Данное действие отображено в связующей таблице FILMS_LIKES.

Пользователи могут состоять в друзьях.Дружба может быть подтвержденная и не подтвержденная. Эта функция отображена в таблице USERS_friend. ("Друг" это тот же пользователь, поэтому оба поля таблицы отсылаются на таблицу USER).

У ФИЛЬМОВ есть два свойства: жанр (у одного фильма может быть несколько жанров). для этого создана промежуточная таблица FILMES_GANRES.
И рейтинг. Рейтинг только однин. так что таблица FILM_RATING связана с FILMS напрямую.


![Untitled-2](https://github.com/OlgaVolynka/java-filmorate/assets/119079339/811a3494-efb3-4388-9706-45fcd954e174)


Дальнейшая разработка данного приложения была реализована в групповом проекте.
[https://github.com/CyberCoHuK/java-filmorate](https://github.com/GlazyrinAV/java-filmorate)https://github.com/GlazyrinAV/java-filmorate
