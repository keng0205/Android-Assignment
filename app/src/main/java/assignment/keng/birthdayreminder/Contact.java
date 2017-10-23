package assignment.keng.birthdayreminder;


public class Contact implements java.io.Serializable {
    private long id;
    private String name;
    private String email;
    private String birthDate;

    public Contact() {}

    public Contact(String name, String email, String birthDate) {
        this.id = 0;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
    }

    public Contact(long id, String name, String email, String birthDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
    }

    public long getId() {return id;}
    public String getName() {return name;}
    public String getEmail() {return email;}
    public String getBirthDate() {return birthDate;}

    public void setId(long id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setEmail(String email) {this.email = email;}
    public void setBirthDate(String birthDate) {this.birthDate = birthDate;}
}
