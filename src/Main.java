import java.util.List;

public class Main {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("PersonPU");

    public static <TypedQuery> void main(String[] args) {
        // Задание 1: сериализация и десериализация класса Person в файл
        Person person = new Person("Alice", 25);

        // Сериализация объекта Person в файл
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("person.ser"))) {
            oos.writeObject(person);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Десериализация объекта Person из файла
        Person deserializedPerson = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("person.ser"))) {
            deserializedPerson = (Person) ois.readObject();
            System.out.println("Deserialized Person: " + deserializedPerson);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Задание 2: использование JPA для работы с базой данных
        EntityManager em = emf.createEntityManager();

        // Добавление объекта Person в базу данных
        em.getTransaction().begin();
        em.persist(person);
        em.getTransaction().commit();

        // Обновление объекта Person в базе данных
        em.getTransaction().begin();
        person.setAge(26);
        em.merge(person);
        em.getTransaction().commit();

        // Удаление объекта Person из базы данных
        em.getTransaction().begin();
        em.remove(person);
        em.getTransaction().commit();

        // Получение всех объектов Person из базы данных
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p", Person.class);
        List<Person> persons = query.getResultList();
        for (Person p : persons) {
            System.out.println("Person from database: " + p);
        }

        em.close();
        emf.close();
    }
}
