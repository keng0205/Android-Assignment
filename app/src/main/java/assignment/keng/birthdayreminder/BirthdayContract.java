package assignment.keng.birthdayreminder;

import android.provider.BaseColumns;


public class BirthdayContract {

    private BirthdayContract() {}

    public static class ContactEntry implements BaseColumns {
        public static final String TABLE_NAME = "contact";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_BIRTHDATE = "birthdate";
    }
}
