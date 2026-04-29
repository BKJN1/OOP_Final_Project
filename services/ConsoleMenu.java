package services; //a

import academic.Course;
import academic.Enrollment;
import academic.Lesson;
import academic.Mark;
import enums.CourseType;
import enums.Format;
import enums.Language;
import enums.LessonType;
import enums.UrgencyLevel;
import exceptions.UnauthorizedActionException;
import interfaces.CanResearch;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import research.ResearchPaper;
import research.Researcher;
import research.Journal;
import storage.DataManager;
import storage.Database;
import system.News;
import system.Request;
import users.Admin;
import users.Dean;
import users.Employee;
import users.GraduateStudent;
import users.Manager;
import users.Student;
import users.Teacher;
import users.TechSupportSpecialist;
import users.User;

public class ConsoleMenu {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static Database database;
    private static AuthService authService;
    private static CourseService courseService;
    private static NewsService newsService;
    private static ResearchService researchService;
    private static Language currentLanguage = Language.EN;
    private static boolean localizedOutputInstalled;
    private static final Map<String, String> RU = new HashMap<>();
    private static final Map<String, String> KZ = new HashMap<>();

    static {
        addTranslation("\n=== University System ===", "\n=== Университетская система ===", "\n=== Университет жүйесі ===");
        addTranslation("\n=== Admin Menu ===", "\n=== Меню администратора ===", "\n=== Әкімші мәзірі ===");
        addTranslation("\n=== Manager Menu ===", "\n=== Меню менеджера ===", "\n=== Менеджер мәзірі ===");
        addTranslation("\n=== Teacher Menu ===", "\n=== Меню преподавателя ===", "\n=== Оқытушы мәзірі ===");
        addTranslation("\n=== Student Menu ===", "\n=== Меню студента ===", "\n=== Студент мәзірі ===");
        addTranslation("\n=== Dean Menu ===", "\n=== Меню декана ===", "\n=== Декан мәзірі ===");
        addTranslation("\n=== Tech Support Menu ===", "\n=== Меню техподдержки ===", "\n=== Техникалық қолдау мәзірі ===");
        addTranslation("\n=== Research Menu ===", "\n=== Меню исследований ===", "\n=== Зерттеу мәзірі ===");
        addTranslation("\n=== Journals ===", "\n=== Журналы ===", "\n=== Журналдар ===");
        addTranslation("\n=== News Comments ===", "\n=== Комментарии к новостям ===", "\n=== Жаңалық пікірлері ===");
        addTranslation("\n=== News Management ===", "\n=== Управление новостями ===", "\n=== Жаңалықтарды басқару ===");
        addTranslation("\n=== Student Organizations ===", "\n=== Студенческие организации ===", "\n=== Студенттік ұйымдар ===");

        addTranslation("Language: ", "Язык: ", "Тіл: ");
        addTranslation("1. Login", "1. Войти", "1. Кіру");
        addTranslation("2. Exit", "2. Выход", "2. Шығу");
        addTranslation("3. Switch language", "3. Сменить язык", "3. Тілді ауыстыру");
        addTranslation("Choose: ", "Выберите: ", "Таңдаңыз: ");
        addTranslation("Username: ", "Логин: ", "Логин: ");
        addTranslation("Password: ", "Пароль: ", "Құпиясөз: ");
        addTranslation("Enter regex: ", "Введите regex: ", "Regex енгізіңіз: ");
        addTranslation("Language: ", "Язык: ", "Тіл: ");

        addTranslation("1. View users", "1. Посмотреть пользователей", "1. Пайдаланушыларды көру");
        addTranslation("2. Add sample student", "2. Добавить студента", "2. Студент қосу");
        addTranslation("3. Update user", "3. Обновить пользователя", "3. Пайдаланушыны жаңарту");
        addTranslation("4. Remove user", "4. Удалить пользователя", "4. Пайдаланушыны жою");
        addTranslation("5. View logs", "5. Посмотреть логи", "5. Логтарды көру");
        addTranslation("6. Journals", "6. Журналы", "6. Журналдар");
        addTranslation("7. Logout", "7. Выйти из аккаунта", "7. Аккаунттан шығу");

        addTranslation("1. View courses", "1. Посмотреть курсы", "1. Курстарды көру");
        addTranslation("2. Add course", "2. Добавить курс", "2. Курс қосу");
        addTranslation("3. Assign course to teacher", "3. Назначить курс преподавателю", "3. Курсты оқытушыға тағайындау");
        addTranslation("4. Approve students registration", "4. Одобрить регистрацию студентов", "4. Студент тіркелуін мақұлдау");
        addTranslation("5. Create academic report", "5. Создать академический отчёт", "5. Академиялық есеп жасау");
        addTranslation("6. Manage news", "6. Управлять новостями", "6. Жаңалықтарды басқару");
        addTranslation("7. View students by GPA", "7. Студенты по GPA", "7. GPA бойынша студенттер");
        addTranslation("8. View students alphabetically", "8. Студенты по алфавиту", "8. Әліпби бойынша студенттер");
        addTranslation("9. View employee requests", "9. Запросы сотрудников", "9. Қызметкерлер сұраныстары");
        addTranslation("10. View teachers alphabetically", "10. Преподаватели по алфавиту", "10. Әліпби бойынша оқытушылар");
        addTranslation("11. Journals", "11. Журналы", "11. Журналдар");
        addTranslation("12. Logout", "12. Выйти из аккаунта", "12. Аккаунттан шығу");

        addTranslation("1. View my courses", "1. Мои курсы", "1. Менің курстарым");
        addTranslation("2. View students of course", "2. Студенты курса", "2. Курстың студенттері");
        addTranslation("3. Manage course", "3. Управлять курсом", "3. Курсты басқару");
        addTranslation("4. Put mark", "4. Поставить оценку", "4. Баға қою");
        addTranslation("5. Send message to employee", "5. Отправить сообщение сотруднику", "5. Қызметкерге хабарлама жіберу");
        addTranslation("6. Send complaint to dean", "6. Отправить жалобу декану", "6. Деканға шағым жіберу");
        addTranslation("7. Research menu", "7. Меню исследований", "7. Зерттеу мәзірі");
        addTranslation("8. Journals", "8. Журналы", "8. Журналдар");
        addTranslation("9. Logout", "9. Выйти из аккаунта", "9. Аккаунттан шығу");

        addTranslation("2. Register for course", "2. Зарегистрироваться на курс", "2. Курсқа тіркелу");
        addTranslation("3. View teacher of course", "3. Преподаватель курса", "3. Курстың оқытушысы");
        addTranslation("4. View marks", "4. Посмотреть оценки", "4. Бағаларды көру");
        addTranslation("5. View transcript", "5. Посмотреть транскрипт", "5. Транскрипт көру");
        addTranslation("6. Rate teacher", "6. Оценить преподавателя", "6. Оқытушыны бағалау");
        addTranslation("7. View notifications", "7. Уведомления", "7. Хабарламалар");
        addTranslation("8. Student organizations", "8. Студенческие организации", "8. Студенттік ұйымдар");
        addTranslation("9. Journals", "9. Журналы", "9. Журналдар");
        addTranslation("10. News comments", "10. Комментарии к новостям", "10. Жаңалық пікірлері");
        addTranslation("11. Research menu", "11. Меню исследований", "11. Зерттеу мәзірі");
        addTranslation("12. Logout", "12. Выйти из аккаунта", "12. Аккаунттан шығу");

        addTranslation("1. View new requests", "1. Новые запросы", "1. Жаңа сұраныстар");
        addTranslation("2. Accept request", "2. Принять запрос", "2. Сұранысты қабылдау");
        addTranslation("3. Reject request", "3. Отклонить запрос", "3. Сұранысты қабылдамау");
        addTranslation("4. Mark request done", "4. Отметить выполненным", "4. Орындалды деп белгілеу");
        addTranslation("5. View all requests", "5. Все запросы", "5. Барлық сұраныстар");
        addTranslation("1. View requests", "1. Посмотреть запросы", "1. Сұраныстарды көру");
        addTranslation("2. Sign request", "2. Подписать запрос", "2. Сұранысқа қол қою");
        addTranslation("3. Send message to employee", "3. Отправить сообщение сотруднику", "3. Қызметкерге хабарлама жіберу");
        addTranslation("4. Journals", "4. Журналы", "4. Журналдар");
        addTranslation("5. Logout", "5. Выйти из аккаунта", "5. Аккаунттан шығу");

        addTranslation("1. View course details", "1. Детали курса", "1. Курс мәліметтері");
        addTranslation("2. Update title", "2. Изменить название", "2. Атауын өзгерту");
        addTranslation("3. Update credits", "3. Изменить кредиты", "3. Кредит санын өзгерту");
        addTranslation("4. Update course type", "4. Изменить тип курса", "4. Курс түрін өзгерту");
        addTranslation("5. Add lesson", "5. Добавить занятие", "5. Сабақ қосу");
        addTranslation("6. View lessons", "6. Посмотреть занятия", "6. Сабақтарды көру");
        addTranslation("7. Back", "7. Назад", "7. Артқа");

        addTranslation("1. View my h-index", "1. Мой h-index", "1. Менің h-index");
        addTranslation("2. Print my papers by citations", "2. Мои статьи по цитированиям", "2. Мақалаларымды citation бойынша шығару");
        addTranslation("3. Print all university papers by date", "3. Все статьи университета по дате", "3. Университет мақалаларын дата бойынша шығару");
        addTranslation("4. Print top cited researcher", "4. Самый цитируемый исследователь", "4. Ең көп citation алған зерттеуші");
        addTranslation("5. Print top cited researcher by school", "5. Топ исследователь по школе", "5. Мектеп бойынша топ зерттеуші");
        addTranslation("6. Print top cited researcher of year", "6. Топ исследователь года", "6. Жылдың топ зерттеушісі");
        addTranslation("7. Show citation for my first paper", "7. Цитирование первой статьи", "7. Бірінші мақаламның citation көрсету");
        addTranslation("8. Add first paper as diploma project", "8. Добавить статью как диплом", "8. Мақаланы диплом жобасы ретінде қосу");
        addTranslation("9. View diploma papers", "9. Дипломные статьи", "9. Диплом мақалалары");
        addTranslation("10. Back", "10. Назад", "10. Артқа");

        addTranslation("1. View journals", "1. Посмотреть журналы", "1. Журналдарды көру");
        addTranslation("2. Subscribe", "2. Подписаться", "2. Жазылу");
        addTranslation("3. Unsubscribe", "3. Отписаться", "3. Жазылымнан шығу");
        addTranslation("4. Back", "4. Назад", "4. Артқа");
        addTranslation("1. View news", "1. Посмотреть новости", "1. Жаңалықтарды көру");
        addTranslation("2. Add comment", "2. Добавить комментарий", "2. Пікір қосу");
        addTranslation("3. View comments", "3. Посмотреть комментарии", "3. Пікірлерді көру");
        addTranslation("1. Publish news", "1. Опубликовать новость", "1. Жаңалық жариялау");
        addTranslation("2. View news", "2. Посмотреть новости", "2. Жаңалықтарды көру");
        addTranslation("3. Add comment", "3. Добавить комментарий", "3. Пікір қосу");
        addTranslation("4. View comments", "4. Посмотреть комментарии", "4. Пікірлерді көру");
        addTranslation("5. Back", "5. Назад", "5. Артқа");
        addTranslation("1. View my organizations", "1. Мои организации", "1. Менің ұйымдарым");
        addTranslation("2. Join organization as member", "2. Вступить как участник", "2. Мүше ретінде қосылу");
        addTranslation("3. Become head of organization", "3. Стать главой организации", "3. Ұйым басшысы болу");

        addTranslation("Authenticate before search.", "Авторизуйтесь перед поиском.", "Іздеу алдында жүйеге кіріңіз.");
        addTranslation("Invalid option.", "Неверный вариант.", "Қате таңдау.");
        addTranslation("Enter a number.", "Введите число.", "Сан енгізіңіз.");
        addTranslation("Assigned.", "Назначено.", "Тағайындалды.");
        addTranslation("Comment added.", "Комментарий добавлен.", "Пікір қосылды.");
        addTranslation("Complaint sent.", "Жалоба отправлена.", "Шағым жіберілді.");
        addTranslation("Course added.", "Курс добавлен.", "Курс қосылды.");
        addTranslation("Course type updated.", "Тип курса обновлён.", "Курс түрі жаңартылды.");
        addTranslation("Credits updated.", "Кредиты обновлены.", "Кредиттер жаңартылды.");
        addTranslation("Data saved to university.dat", "Данные сохранены в university.dat", "Деректер university.dat файлына сақталды");
        addTranslation("Invalid course.", "Неверный курс.", "Қате курс.");
        addTranslation("Invalid user.", "Неверный пользователь.", "Қате пайдаланушы.");
        addTranslation("Joined as member.", "Вы вступили как участник.", "Мүше ретінде қосылдыңыз.");
        addTranslation("Leave field empty to keep current value.", "Оставьте поле пустым, чтобы сохранить текущее значение.", "Ағымдағы мәнді қалдыру үшін бос қалдырыңыз.");
        addTranslation("Lesson added.", "Занятие добавлено.", "Сабақ қосылды.");
        addTranslation("Mark saved.", "Оценка сохранена.", "Баға сақталды.");
        addTranslation("Message sent.", "Сообщение отправлено.", "Хабарлама жіберілді.");
        addTranslation("News published.", "Новость опубликована.", "Жаңалық жарияланды.");
        addTranslation("No menu for this role.", "Для этой роли меню нет.", "Бұл рөл үшін мәзір жоқ.");
        addTranslation("No organizations.", "Организаций нет.", "Ұйымдар жоқ.");
        addTranslation("No papers.", "Статей нет.", "Мақалалар жоқ.");
        addTranslation("No pending registration selected.", "Заявка на регистрацию не выбрана.", "Тіркелу өтініші таңдалмады.");
        addTranslation("No pending registrations.", "Ожидающих регистраций нет.", "Күтіп тұрған тіркелулер жоқ.");
        addTranslation("No research papers to add.", "Нет research papers для добавления.", "Қосатын research paper жоқ.");
        addTranslation("Rating sent.", "Оценка отправлена.", "Баға жіберілді.");
        addTranslation("Registration approved.", "Регистрация одобрена.", "Тіркелу мақұлданды.");
        addTranslation("Registration request sent. Manager must approve it.", "Заявка отправлена. Менеджер должен одобрить.", "Өтініш жіберілді. Менеджер мақұлдауы керек.");
        addTranslation("Request signed.", "Запрос подписан.", "Сұранысқа қол қойылды.");
        addTranslation("Request updated.", "Запрос обновлён.", "Сұраныс жаңартылды.");
        addTranslation("Student added.", "Студент добавлен.", "Студент қосылды.");
        addTranslation("Subscribed.", "Подписка оформлена.", "Жазылым жасалды.");
        addTranslation("This user is not a researcher.", "Этот пользователь не исследователь.", "Бұл пайдаланушы зерттеуші емес.");
        addTranslation("Title updated.", "Название обновлено.", "Атауы жаңартылды.");
        addTranslation("Unsubscribed.", "Подписка отменена.", "Жазылым тоқтатылды.");
        addTranslation("User removed if id existed.", "Пользователь удалён, если id существовал.", "ID бар болса, пайдаланушы жойылды.");
        addTranslation("You are head of this organization.", "Вы глава этой организации.", "Сіз осы ұйымның басшысысыз.");
    }

    public ConsoleMenu(Database database) {
        installLocalizedOutput();
        ConsoleMenu.database = database;
        authService = new AuthService(database);
        courseService = new CourseService(database);
        newsService = new NewsService(database);
        researchService = new ResearchService(database);
    }

    private static void installLocalizedOutput() {
        if (!localizedOutputInstalled) {
            System.setOut(new LocalizedPrintStream(System.out));
            localizedOutputInstalled = true;
        }
    }

    private static void addTranslation(String english, String russian, String kazakh) {
        RU.put(english, russian);
        KZ.put(english, kazakh);
    }

    private static String translate(String text) {
        if (currentLanguage == Language.EN || text == null) {
            return text;
        }
        Map<String, String> dictionary = currentLanguage == Language.RU ? RU : KZ;
        String translated = dictionary.get(text);
        if (translated != null) {
            return translated;
        }
        for (Map.Entry<String, String> entry : dictionary.entrySet()) {
            if (text.startsWith(entry.getKey()) && entry.getKey().endsWith(": ")) {
                return entry.getValue() + text.substring(entry.getKey().length());
            }
        }
        if (text.startsWith("Language: ")) {
            return dictionary.get("Language: ") + text.substring("Language: ".length());
        }
        if (text.startsWith("Logged in as ")) {
            return (currentLanguage == Language.RU ? "Вход выполнен как " : "Жүйеге кірді: ")
                    + text.substring("Logged in as ".length());
        }
        if (text.startsWith("Language switched to ")) {
            return (currentLanguage == Language.RU ? "Язык изменён на " : "Тіл ауыстырылды: ")
                    + text.substring("Language switched to ".length());
        }
        if (text.startsWith("Could not save data: ")) {
            return (currentLanguage == Language.RU ? "Не удалось сохранить данные: " : "Деректерді сақтау мүмкін болмады: ")
                    + text.substring("Could not save data: ".length());
        }
        if (text.startsWith("Invalid regex: ")) {
            return (currentLanguage == Language.RU ? "Неверный regex: " : "Қате regex: ")
                    + text.substring("Invalid regex: ".length());
        }
        if (text.startsWith("User updated: ")) {
            return (currentLanguage == Language.RU ? "Пользователь обновлён: " : "Пайдаланушы жаңартылды: ")
                    + text.substring("User updated: ".length());
        }
        if (text.startsWith("Added diploma paper: ")) {
            return (currentLanguage == Language.RU ? "Дипломная статья добавлена: " : "Диплом мақаласы қосылды: ")
                    + text.substring("Added diploma paper: ".length());
        }
        return text;
    }

    private static String render(String text) {
        String translated = translate(text);
        return currentLanguage == Language.KZ ? transliterateKazakh(translated) : translated;
    }

    private static String transliterateKazakh(String text) {
        return text
                .replace("Ә", "A").replace("ә", "a")
                .replace("Ғ", "G").replace("ғ", "g")
                .replace("Қ", "Q").replace("қ", "q")
                .replace("Ң", "N").replace("ң", "n")
                .replace("Ө", "O").replace("ө", "o")
                .replace("Ұ", "U").replace("ұ", "u")
                .replace("Ү", "U").replace("ү", "u")
                .replace("Һ", "H").replace("һ", "h")
                .replace("І", "I").replace("і", "i")
                .replace("А", "A").replace("а", "a")
                .replace("Б", "B").replace("б", "b")
                .replace("В", "V").replace("в", "v")
                .replace("Г", "G").replace("г", "g")
                .replace("Д", "D").replace("д", "d")
                .replace("Е", "E").replace("е", "e")
                .replace("Ё", "Yo").replace("ё", "yo")
                .replace("Ж", "Zh").replace("ж", "zh")
                .replace("З", "Z").replace("з", "z")
                .replace("И", "I").replace("и", "i")
                .replace("Й", "I").replace("й", "i")
                .replace("К", "K").replace("к", "k")
                .replace("Л", "L").replace("л", "l")
                .replace("М", "M").replace("м", "m")
                .replace("Н", "N").replace("н", "n")
                .replace("О", "O").replace("о", "o")
                .replace("П", "P").replace("п", "p")
                .replace("Р", "R").replace("р", "r")
                .replace("С", "S").replace("с", "s")
                .replace("Т", "T").replace("т", "t")
                .replace("У", "U").replace("у", "u")
                .replace("Ф", "F").replace("ф", "f")
                .replace("Х", "H").replace("х", "h")
                .replace("Ц", "Ts").replace("ц", "ts")
                .replace("Ч", "Ch").replace("ч", "ch")
                .replace("Ш", "Sh").replace("ш", "sh")
                .replace("Щ", "Sh").replace("щ", "sh")
                .replace("Ы", "Y").replace("ы", "y")
                .replace("Э", "E").replace("э", "e")
                .replace("Ю", "Yu").replace("ю", "yu")
                .replace("Я", "Ya").replace("я", "ya")
                .replace("Ь", "").replace("ь", "")
                .replace("Ъ", "").replace("ъ", "");
    }

    private static class LocalizedPrintStream extends PrintStream {
        LocalizedPrintStream(PrintStream out) {
            super(out, true);
        }

        @Override
        public void print(String value) {
            super.print(render(value));
        }

        @Override
        public void println(String value) {
            super.println(render(value));
        }

        @Override
        public void println(Object value) {
            super.println(render(String.valueOf(value)));
        }
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== University System ===");
            System.out.println("Language: " + currentLanguage);
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.println("3. Switch language");
            int choice = readInt("Choose: ");

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    saveBeforeExit();
                    running = false;
                    break;
                case 3:
                    switchLanguage();
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void login() {
        String username = readLine("Username: ");
        String password = readLine("Password: ");
        try {
            User user = authService.login(username, password);
            System.out.println("Logged in as " + user.getFullName() + " (" + user.getClass().getSimpleName() + ")");
            showRoleMenu(user);
        } catch (UnauthorizedActionException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void showRoleMenu(User user) {
        if (user instanceof Admin) {
            adminMenu((Admin) user);
        } else if (user instanceof Manager) {
            managerMenu((Manager) user);
        } else if (user instanceof Teacher) {
            teacherMenu((Teacher) user);
        } else if (user instanceof TechSupportSpecialist) {
            techSupportMenu((TechSupportSpecialist) user);
        } else if (user instanceof Dean) {
            deanMenu((Dean) user);
        } else if (user instanceof GraduateStudent) {
            studentMenu((Student) user);
        } else if (user instanceof Student) {
            studentMenu((Student) user);
        } else {
            System.out.println("No menu for this role.");
        }
        authService.logout();
    }

    private static void adminMenu(Admin admin) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. View users");
            System.out.println("2. Add sample student");
            System.out.println("3. Update user");
            System.out.println("4. Remove user");
            System.out.println("5. View logs");
            System.out.println("6. Journals");
            System.out.println("7. Logout");
            switch (readInt("Choose: ")) {
                case 1:
                    database.getUsers().forEach(System.out::println);
                    break;
                case 2:
                    Student student = new Student(readLine("Id: "), readLine("Username: "), readLine("Password: "),
                            readLine("Full name: "), readLine("Email: "), readLine("Major: "), readInt("Year: "));
                    admin.addUser(database, student);
                    System.out.println("Student added.");
                    break;
                case 3:
                    updateUser(admin);
                    break;
                case 4:
                    admin.removeUser(database, readLine("User id: "));
                    System.out.println("User removed if id existed.");
                    break;
                case 5:
                    database.getLogs().forEach(System.out::println);
                    break;
                case 6:
                    journalMenu(admin);
                    break;
                case 7:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void managerMenu(Manager manager) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Manager Menu ===");
            System.out.println("1. View courses");
            System.out.println("2. Add course");
            System.out.println("3. Assign course to teacher");
            System.out.println("4. Approve students registration");
            System.out.println("5. Create academic report");
            System.out.println("6. Manage news");
            System.out.println("7. View students by GPA");
            System.out.println("8. View students alphabetically");
            System.out.println("9. View employee requests");
            System.out.println("10. View teachers alphabetically");
            System.out.println("11. Journals");
            System.out.println("12. Logout");
            switch (readInt("Choose: ")) {
                case 1:
                    database.getCourses().forEach(System.out::println);
                    break;
                case 2:
                    addCourse(manager);
                    break;
                case 3:
                    assignCourseToTeacher(manager);
                    break;
                case 4:
                    approveStudentRegistration();
                    break;
                case 5:
                    System.out.println(manager.createAcademicReport(database));
                    break;
                case 6:
                    newsManagementMenu(manager);
                    break;
                case 7:
                    manager.viewStudentsSorted(database, Student.byGpa()).forEach(System.out::println);
                    break;
                case 8:
                    manager.viewStudentsSorted(database, Comparator.comparing(Student::getFullName)).forEach(System.out::println);
                    break;
                case 9:
                    database.getRequests().forEach(System.out::println);
                    break;
                case 10:
                    database.getUsers().stream()
                            .filter(Teacher.class::isInstance)
                            .map(Teacher.class::cast)
                            .sorted(Teacher.byName())
                            .forEach(System.out::println);
                    break;
                case 11:
                    journalMenu(manager);
                    break;
                case 12:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void teacherMenu(Teacher teacher) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Teacher Menu ===");
            System.out.println("1. View my courses");
            System.out.println("2. View students of course");
            System.out.println("3. Manage course");
            System.out.println("4. Put mark");
            System.out.println("5. Send message to employee");
            System.out.println("6. Send complaint to dean");
            System.out.println("7. Research menu");
            System.out.println("8. Journals");
            System.out.println("9. Logout");
            switch (readInt("Choose: ")) {
                case 1:
                    teacher.getCourses().forEach(System.out::println);
                    break;
                case 2:
                    Course course = chooseCourse();
                    if (course != null) {
                        teacher.viewStudents(course).forEach(System.out::println);
                    }
                    break;
                case 3:
                    manageCourse(teacher);
                    break;
                case 4:
                    putMark(teacher);
                    break;
                case 5:
                    sendEmployeeMessage(teacher);
                    break;
                case 6:
                    sendComplaint(teacher);
                    break;
                case 7:
                    researchMenu(teacher);
                    break;
                case 8:
                    journalMenu(teacher);
                    break;
                case 9:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void studentMenu(Student student) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Student Menu ===");
            System.out.println("1. View courses");
            System.out.println("2. Register for course");
            System.out.println("3. View teacher of course");
            System.out.println("4. View marks");
            System.out.println("5. View transcript");
            System.out.println("6. Rate teacher");
            System.out.println("7. View notifications");
            System.out.println("8. Student organizations");
            System.out.println("9. Journals");
            System.out.println("10. News comments");
            System.out.println("11. Research menu");
            System.out.println("12. Logout");
            switch (readInt("Choose: ")) {
                case 1:
                    database.getCourses().forEach(System.out::println);
                    break;
                case 2:
                    registerForCourse(student);
                    break;
                case 3:
                    Course course = chooseCourse();
                    if (course != null) {
                        course.getInstructors().forEach(System.out::println);
                    }
                    break;
                case 4:
                    student.getMarks().forEach((c, m) -> System.out.println(c.getCode() + ": " + m));
                    break;
                case 5:
                    System.out.println(student.getTranscript());
                    break;
                case 6:
                    Teacher teacher = chooseTeacher();
                    if (teacher != null) {
                        student.rateTeacher(teacher, readInt("Rating 1-5: "));
                        System.out.println("Rating sent.");
                    }
                    break;
                case 7:
                    student.getNotifications().forEach(System.out::println);
                    break;
                case 8:
                    studentOrganizationsMenu(student);
                    break;
                case 9:
                    journalMenu(student);
                    break;
                case 10:
                    newsCommentsMenu(student);
                    break;
                case 11:
                    researchMenu(student);
                    break;
                case 12:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void techSupportMenu(TechSupportSpecialist support) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Tech Support Menu ===");
            System.out.println("1. View new requests");
            System.out.println("2. Accept request");
            System.out.println("3. Reject request");
            System.out.println("4. Mark request done");
            System.out.println("5. View all requests");
            System.out.println("6. Journals");
            System.out.println("7. Logout");
            switch (readInt("Choose: ")) {
                case 1:
                    support.seeNewRequests(database).forEach(System.out::println);
                    break;
                case 2:
                    changeRequestStatus(support, "accept");
                    break;
                case 3:
                    changeRequestStatus(support, "reject");
                    break;
                case 4:
                    changeRequestStatus(support, "done");
                    break;
                case 5:
                    database.getRequests().forEach(System.out::println);
                    break;
                case 6:
                    journalMenu(support);
                    break;
                case 7:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void deanMenu(Dean dean) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Dean Menu ===");
            System.out.println("1. View requests");
            System.out.println("2. Sign request");
            System.out.println("3. Send message to employee");
            System.out.println("4. Journals");
            System.out.println("5. Logout");
            switch (readInt("Choose: ")) {
                case 1:
                    database.getRequests().forEach(System.out::println);
                    break;
                case 2:
                    Request request = chooseRequest();
                    if (request != null) {
                        dean.signRequest(request);
                        System.out.println("Request signed.");
                    }
                    break;
                case 3:
                    sendEmployeeMessage(dean);
                    break;
                case 4:
                    journalMenu(dean);
                    break;
                case 5:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void researchMenu(User user) {
        if (!(user instanceof CanResearch) || !((CanResearch) user).isResearcher()) {
            System.out.println("This user is not a researcher.");
            return;
        }
        Researcher researcher = ((CanResearch) user).getResearchProfile();
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Research Menu ===");
            System.out.println("1. View my h-index");
            System.out.println("2. Print my papers by citations");
            System.out.println("3. Print all university papers by date");
            System.out.println("4. Print top cited researcher");
            System.out.println("5. Print top cited researcher by school");
            System.out.println("6. Print top cited researcher of year");
            System.out.println("7. Show citation for my first paper");
            if (user instanceof GraduateStudent) {
                System.out.println("8. Add first paper as diploma project");
                System.out.println("9. View diploma papers");
                System.out.println("10. Back");
            } else {
                System.out.println("8. Back");
            }
            switch (readInt("Choose: ")) {
                case 1:
                    System.out.println("h-index: " + researcher.calculateHIndex());
                    break;
                case 2:
                    researcher.printPapers(ResearchPaper.byCitations());
                    break;
                case 3:
                    researchService.printAllPapers(ResearchPaper.byDate()).forEach(System.out::println);
                    break;
                case 4:
                    System.out.println(researchService.topCitedResearcher());
                    break;
                case 5:
                    System.out.println(researchService.topCitedResearcherBySchool(readLine("School/major/department: ")));
                    break;
                case 6:
                    System.out.println(researchService.topCitedResearcherOfYear(readInt("Year: ")));
                    break;
                case 7:
                    if (researcher.getPapers().isEmpty()) {
                        System.out.println("No papers.");
                    } else {
                        ResearchPaper paper = researcher.getPapers().get(0);
                        System.out.println(paper.getCitation(Format.PLAIN_TEXT));
                        System.out.println(paper.getCitation(Format.BIBTEX));
                    }
                    break;
                case 8:
                    if (user instanceof GraduateStudent) {
                        addDiplomaPaper((GraduateStudent) user, researcher);
                    } else {
                        back = true;
                    }
                    break;
                case 9:
                    if (user instanceof GraduateStudent) {
                        ((GraduateStudent) user).getDiplomaPapers().forEach(System.out::println);
                    } else {
                        System.out.println("Invalid option.");
                    }
                    break;
                case 10:
                    if (user instanceof GraduateStudent) {
                        back = true;
                    } else {
                        System.out.println("Invalid option.");
                    }
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void newsManagementMenu(Manager manager) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== News Management ===");
            System.out.println("1. Publish news");
            System.out.println("2. View news");
            System.out.println("3. Add comment");
            System.out.println("4. View comments");
            System.out.println("5. Back");
            switch (readInt("Choose: ")) {
                case 1:
                    manager.publishNews(newsService, readLine("Topic: "), readLine("Content: "));
                    System.out.println("News published.");
                    break;
                case 2:
                    database.getNews().forEach(System.out::println);
                    break;
                case 3:
                    News news = chooseNews();
                    if (news != null) {
                        news.addComment(manager, readLine("Comment: "));
                        System.out.println("Comment added.");
                    }
                    break;
                case 4:
                    News item = chooseNews();
                    if (item != null) {
                        item.getComments().forEach(System.out::println);
                    }
                    break;
                case 5:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void switchLanguage() {
        System.out.println("1. KZ");
        System.out.println("2. EN");
        System.out.println("3. RU");
        int choice = readInt("Language: ");
        if (choice == 1) {
            currentLanguage = Language.KZ;
        } else if (choice == 3) {
            currentLanguage = Language.RU;
        } else {
            currentLanguage = Language.EN;
        }
        System.out.println("Language switched to " + currentLanguage + ".");
    }

    private static void journalMenu(User user) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Journals ===");
            System.out.println("1. View journals");
            System.out.println("2. Subscribe");
            System.out.println("3. Unsubscribe");
            System.out.println("4. Back");
            switch (readInt("Choose: ")) {
                case 1:
                    database.getJournals().forEach(System.out::println);
                    break;
                case 2:
                    Journal journal = chooseJournal();
                    if (journal != null) {
                        journal.subscribe(user);
                        System.out.println("Subscribed.");
                    }
                    break;
                case 3:
                    Journal selected = chooseJournal();
                    if (selected != null) {
                        selected.unsubscribe(user);
                        System.out.println("Unsubscribed.");
                    }
                    break;
                case 4:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void newsCommentsMenu(User user) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== News Comments ===");
            System.out.println("1. View news");
            System.out.println("2. Add comment");
            System.out.println("3. View comments");
            System.out.println("4. Back");
            switch (readInt("Choose: ")) {
                case 1:
                    database.getNews().forEach(System.out::println);
                    break;
                case 2:
                    News news = chooseNews();
                    if (news != null) {
                        news.addComment(user, readLine("Comment: "));
                        System.out.println("Comment added.");
                    }
                    break;
                case 3:
                    News item = chooseNews();
                    if (item != null) {
                        item.getComments().forEach(System.out::println);
                    }
                    break;
                case 4:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void addDiplomaPaper(GraduateStudent student, Researcher researcher) {
        if (researcher.getPapers().isEmpty()) {
            System.out.println("No research papers to add.");
            return;
        }
        ResearchPaper paper = researcher.getPapers().get(0);
        student.addDiplomaPaper(paper);
        System.out.println("Added diploma paper: " + paper);
    }

    private static void addCourse(Manager manager) {
        String code = readLine("Code: ");
        String title = readLine("Title: ");
        int credits = readInt("Credits: ");
        CourseType type = readCourseType();
        String major = readLine("Intended major: ");
        int year = readInt("Intended year: ");
        manager.addCourse(database, code, title, credits, type, major, year);
        System.out.println("Course added.");
    }

    private static void approveStudentRegistration() {
        Enrollment enrollment = choosePendingEnrollment();
        if (enrollment == null) {
            System.out.println("No pending registration selected.");
            return;
        }
        try {
            courseService.approveEnrollment(enrollment);
            System.out.println("Registration approved.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void manageCourse(Teacher teacher) {
        Course course = chooseTeacherCourse(teacher);
        if (course == null) {
            System.out.println("Invalid course.");
            return;
        }

        boolean back = false;
        while (!back) {
            System.out.println("\n=== Manage Course: " + course.getCode() + " ===");
            System.out.println("1. View course details");
            System.out.println("2. Update title");
            System.out.println("3. Update credits");
            System.out.println("4. Update course type");
            System.out.println("5. Add lesson");
            System.out.println("6. View lessons");
            System.out.println("7. Back");
            switch (readInt("Choose: ")) {
                case 1:
                    System.out.println(course);
                    System.out.println("Major/year: " + course.getIntendedMajor() + "/" + course.getIntendedYear());
                    System.out.println("Instructors: " + course.getInstructors());
                    System.out.println("Students: " + course.getStudents());
                    break;
                case 2:
                    course.setTitle(readLine("New title: "));
                    System.out.println("Title updated.");
                    break;
                case 3:
                    course.setCredits(readInt("New credits: "));
                    System.out.println("Credits updated.");
                    break;
                case 4:
                    course.setType(readCourseType());
                    System.out.println("Course type updated.");
                    break;
                case 5:
                    Lesson lesson = new Lesson(readLessonType(), readLine("Room: "), readLine("Time: "), teacher);
                    course.addLesson(lesson);
                    System.out.println("Lesson added.");
                    break;
                case 6:
                    course.getLessons().forEach(System.out::println);
                    break;
                case 7:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void studentOrganizationsMenu(Student student) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Student Organizations ===");
            System.out.println("1. View my organizations");
            System.out.println("2. Join organization as member");
            System.out.println("3. Become head of organization");
            System.out.println("4. Back");
            switch (readInt("Choose: ")) {
                case 1:
                    if (student.getOrganizations().isEmpty()) {
                        System.out.println("No organizations.");
                    } else {
                        student.getOrganizations().forEach((name, role) -> System.out.println(name + " - " + role));
                    }
                    break;
                case 2:
                    student.joinOrganization(readLine("Organization name: "));
                    System.out.println("Joined as member.");
                    break;
                case 3:
                    student.leadOrganization(readLine("Organization name: "));
                    System.out.println("You are head of this organization.");
                    break;
                case 4:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void updateUser(Admin admin) {
        User user = chooseUser();
        if (user == null) {
            System.out.println("Invalid user.");
            return;
        }
        System.out.println("Leave field empty to keep current value.");
        String username = readLine("New username [" + user.getUsername() + "]: ");
        String password = readLine("New password: ");
        String fullName = readLine("New full name [" + user.getFullName() + "]: ");
        String email = readLine("New email [" + user.getEmail() + "]: ");
        admin.updateUser(database, user, username, password, fullName, email);
        System.out.println("User updated: " + user);
    }

    private static void assignCourseToTeacher(Manager manager) {
        Course course = chooseCourse();
        Teacher teacher = chooseTeacher();
        if (course != null && teacher != null) {
            manager.assignCourseToTeacher(course, teacher);
            System.out.println("Assigned.");
        }
    }

    private static void putMark(Teacher teacher) {
        Student student = chooseStudent();
        Course course = chooseCourse();
        if (student != null && course != null) {
            Mark mark = new Mark(readInt("1st attestation: "), readInt("2nd attestation: "), readInt("Final: "));
            teacher.putMark(student, course, mark);
            System.out.println("Mark saved.");
        }
    }

    private static void registerForCourse(Student student) {
        Course course = chooseCourse();
        if (course == null) {
            return;
        }
        try {
            courseService.register(student, course);
            System.out.println("Registration request sent. Manager must approve it.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sendEmployeeMessage(Employee sender) {
        Employee receiver = chooseEmployee();
        if (receiver != null) {
            sender.sendMessage(receiver, readLine("Message: "));
            System.out.println("Message sent.");
        }
    }

    private static void sendComplaint(Teacher teacher) {
        Student student = chooseStudent();
        Dean dean = chooseDean();
        if (student != null && dean != null) {
            Request request = teacher.sendComplaint(student, dean, UrgencyLevel.HIGH, readLine("Complaint text: "));
            database.addRequest(request);
            System.out.println("Complaint sent.");
        }
    }

    private static void changeRequestStatus(TechSupportSpecialist support, String action) {
        Request request = chooseRequest();
        if (request == null) {
            return;
        }
        if ("accept".equals(action)) {
            support.accept(request);
        } else if ("reject".equals(action)) {
            support.reject(request);
        } else {
            support.done(request);
        }
        System.out.println("Request updated.");
    }

    private static Course chooseCourse() {
        List<Course> courses = database.getCourses();
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i));
        }
        int index = readInt("Course number: ") - 1;
        return index >= 0 && index < courses.size() ? courses.get(index) : null;
    }

    private static Course chooseTeacherCourse(Teacher teacher) {
        List<Course> courses = teacher.getCourses();
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i));
        }
        int index = readInt("Course number: ") - 1;
        return index >= 0 && index < courses.size() ? courses.get(index) : null;
    }

    private static Enrollment choosePendingEnrollment() {
        List<Enrollment> enrollments = database.getPendingEnrollments();
        if (enrollments.isEmpty()) {
            System.out.println("No pending registrations.");
            return null;
        }
        for (int i = 0; i < enrollments.size(); i++) {
            System.out.println((i + 1) + ". " + enrollments.get(i));
        }
        int index = readInt("Registration number: ") - 1;
        return index >= 0 && index < enrollments.size() ? enrollments.get(index) : null;
    }

    private static User chooseUser() {
        List<User> users = database.getUsers();
        for (int i = 0; i < users.size(); i++) {
            System.out.println((i + 1) + ". " + users.get(i) + " username=" + users.get(i).getUsername());
        }
        int index = readInt("User number: ") - 1;
        return index >= 0 && index < users.size() ? users.get(index) : null;
    }

    private static Student chooseStudent() {
        List<Student> students = database.getStudents();
        for (int i = 0; i < students.size(); i++) {
            System.out.println((i + 1) + ". " + students.get(i));
        }
        int index = readInt("Student number: ") - 1;
        return index >= 0 && index < students.size() ? students.get(index) : null;
    }

    private static Teacher chooseTeacher() {
        List<Teacher> teachers = database.getUsers().stream()
                .filter(Teacher.class::isInstance)
                .map(Teacher.class::cast)
                .toList();
        for (int i = 0; i < teachers.size(); i++) {
            System.out.println((i + 1) + ". " + teachers.get(i));
        }
        int index = readInt("Teacher number: ") - 1;
        return index >= 0 && index < teachers.size() ? teachers.get(index) : null;
    }

    private static Employee chooseEmployee() {
        List<Employee> employees = database.getUsers().stream()
                .filter(Employee.class::isInstance)
                .map(Employee.class::cast)
                .toList();
        for (int i = 0; i < employees.size(); i++) {
            System.out.println((i + 1) + ". " + employees.get(i));
        }
        int index = readInt("Employee number: ") - 1;
        return index >= 0 && index < employees.size() ? employees.get(index) : null;
    }

    private static Dean chooseDean() {
        List<Dean> deans = database.getUsers().stream()
                .filter(Dean.class::isInstance)
                .map(Dean.class::cast)
                .toList();
        for (int i = 0; i < deans.size(); i++) {
            System.out.println((i + 1) + ". " + deans.get(i));
        }
        int index = readInt("Dean number: ") - 1;
        return index >= 0 && index < deans.size() ? deans.get(index) : null;
    }

    private static Request chooseRequest() {
        List<Request> requests = database.getRequests();
        for (int i = 0; i < requests.size(); i++) {
            System.out.println((i + 1) + ". " + requests.get(i));
        }
        int index = readInt("Request number: ") - 1;
        return index >= 0 && index < requests.size() ? requests.get(index) : null;
    }

    private static News chooseNews() {
        List<News> news = database.getNews();
        for (int i = 0; i < news.size(); i++) {
            System.out.println((i + 1) + ". " + news.get(i));
        }
        int index = readInt("News number: ") - 1;
        return index >= 0 && index < news.size() ? news.get(index) : null;
    }

    private static Journal chooseJournal() {
        List<Journal> journals = database.getJournals();
        for (int i = 0; i < journals.size(); i++) {
            System.out.println((i + 1) + ". " + journals.get(i));
        }
        int index = readInt("Journal number: ") - 1;
        return index >= 0 && index < journals.size() ? journals.get(index) : null;
    }

    private static CourseType readCourseType() {
        System.out.println("1. MAJOR");
        System.out.println("2. MINOR");
        System.out.println("3. FREE_ELECTIVE");
        int choice = readInt("Course type: ");
        if (choice == 2) {
            return CourseType.MINOR;
        }
        if (choice == 3) {
            return CourseType.FREE_ELECTIVE;
        }
        return CourseType.MAJOR;
    }

    private static LessonType readLessonType() {
        System.out.println("1. LECTURE");
        System.out.println("2. PRACTICE");
        int choice = readInt("Lesson type: ");
        return choice == 2 ? LessonType.PRACTICE : LessonType.LECTURE;
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = SCANNER.nextLine().trim();
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.out.println("Enter a number.");
            }
        }
    }

    private void saveBeforeExit() {
        try {
            DataManager.getInstance("university.dat").save(database);
            System.out.println("Data saved to university.dat");
        } catch (Exception e) {
            System.out.println("Could not save data: " + e.getMessage());
        }
    }
}
