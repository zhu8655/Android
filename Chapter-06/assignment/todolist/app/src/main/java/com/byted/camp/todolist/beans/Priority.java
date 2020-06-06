package com.byted.camp.todolist.beans;

public enum Priority {
    FIRST(2),
    SECOND(1),
    THIRD(0);

    public int val;

    Priority(int _val) {
        val = _val;
    }

    public static Priority from(int _val) {
        for (Priority p : Priority.values()) {
            if (p.val == _val)
                return p;
        }
        return THIRD;
    }
}
