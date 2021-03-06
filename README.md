Разработать WEB-приложение, которое поддерживает заданную функциональность.
Требования к реализации следующие.

1. На основе сущностей предметной области создать классы которые им соответствуют.
2. Классы и методы должны иметь названия, которые отражают их функциональность, классы должны быть грамотно разнесены по пакетам. 
3. Оформление кода должно соответствовать Java Code Convention.
4. Информацию о предметной области хранить в базе данных (любая RDBMS, предпочтительно MySQL).
5. Для доступа к данным использовать API JDBC с использованием пула соединений (не допускается использование ORM фреймверков).
6. Архитектура приложения должна соответствовать шаблону MVC (не допускается использование MVC фреймверков).
7. Приложение должно поддерживать работу с кириллицей (быть многоязычным), в том числе при хранении информации в базе данных:
    1) должна быть возможность переключения языка интерфейса;
    2) должна быть поддержка ввода, вывода и хранения информации, записанной на разных языках;
    3) в качестве поддерживаемых языков выбрать минимум два:
        * на основе кириллицы
        * на основе латиницы
9. При реализации алгоритмов бизнес-логики использовать шаблоны проектирования.
10. Используя сервлеты и JSP, реализовать функциональность, приведенную в постановке задачи.
11. В качестве контейнера сервлетов использовать Apache Tomcat.
12. На страницах JSP применять теги из библиотеки JSTL и разработанные собственные теги:
    * как минимум один custom tag library тег
    * как минимум один tag file тег.
13. При разработке использовать сессии, фильтры, слушатели.
14. Использовать журналирование событий с использованием библиотеки Apache Log4j или Logback.
15. Код должен содержать комментарии документатора для:
    * типов верхнего уровня;
    * нетривиальных методов;
    * конструкторов.
16. Написать модульные тесты которые по максимуму покрывают функциональность.
17. Самостоятельно расширить постановку задачи по функциональности.

________________

Дополнительно, к требованиям изложенным выше, целесообразно также обеспечить выполнение следующих требований.

1. Реализовать разграничение прав доступа пользователей системы к компонентам приложения.
2. Реализовать защиту от повторной отправки данных на сервер при обновлении страницы (PRG).
3. Все поля ввода должны быть с валидацией данных.

===========================================

Периодические издания

Реализовать работу системы Периодические издания.

В системе существует перечень Изданий, которые сгруппированы по темам.

Читатель может оформить Подписку на одно или несколько изданий. 
Незарегистированный пользователь не может оформить подписку.

Для перечня изданий необходимо реализовать возможность:
  * сортировки изданий по названию
  * сортировки изданий по цене
  * выборки изданий по определенной теме
  * поиска издания по названию

Читатель регистрируется в системе и имеет личный кабинет, в котором отображена информация об изданиях, на которые он подписан.

Читатель имеет персональный Счет, который он может пополнить. Средства со счета снимаются во время подписки на издание.

Администратор системы владеет правами:
  * добавления, удаления и редактирования издания
  * блокирования, разблокирования пользователя
