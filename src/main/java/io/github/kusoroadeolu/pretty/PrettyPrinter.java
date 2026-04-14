package io.github.kusoroadeolu.pretty;

import java.lang.reflect.Field;
import java.util.*;

/*
 * Given we have an object o
 * class Object {
 *   int a;
 *   int[] b;
 *   Object o;
 * }
 *
 * Our preferred output
 * Object{
 *   'a' : '1'
 *   'b' : [2, 3, 4]
 *   'o' : {
 *       'a' : '1'
 *       'b' : [2, 3, 4]
 *   }
 * }
 *
 * We start at the object as the root
 * We append the Object's simple name, then LBRACE
 * We then get the object's fields and start our recursive descent
 * We set each field to accessible and get its type
 * if the type is a primitive type or a String, we append ':' then their value
 * if the type is an array or collection or a hashmap, we append ':' then their toString impl
 * other-wise we recurse, we append a ':' then an LBRACE, we then loop over the fields in the object which follows the rules set above. At the end of the loop, we append an '}'
 *  How to handle seen object. If we find a seen object and the object is an object lol, not a primitive or array,
 * */


public class PrettyPrinter {
    private final static char LBRACE = '{';
    private final static char RBRACE = '}';
    private final static char EMPTY = ' ';
    private final static char NEWLINE = '\n';
    private final static String COLON = " : ";
    private final int indentSize;

    public PrettyPrinter(int indentSize) {
        this.indentSize = indentSize;
    }

    public PrettyPrinter() {
        this(2);
    }

    public String print(Object o){
        var sb = new StringBuilder();
        sb.append(o.getClass().getSimpleName())
                .append(LBRACE)
                .append(NEWLINE);
        Set<Object> seen = new HashSet<>();
        recurse(sb, o, 2, seen);
        sb.append(RBRACE);
        return sb.toString();
    }


    public void recurse(StringBuilder sb, Object root , int indent, Set<Object> seen){
        Field[] fields = root.getClass().getDeclaredFields();
        for (Field field : fields){
            field.setAccessible(true);
            Object child = getChild(field, root);
            if (isPrimitive(child) || isString(child) || isCollection(child) || isEnum(child)){
                sb.repeat(EMPTY, indent)
                        .append(field.getName())
                        .append(COLON)
                        .append(child)
                        .append(NEWLINE);
            } else if (isArray(child)) {
                Object[] arr = ((Object[]) child);
                sb.repeat(EMPTY, indent)
                        .append(field.getName())
                        .append(COLON)
                        .append(Arrays.toString(arr))
                        .append(NEWLINE);
            } else if(!seen.add(child)){
                sb.repeat(EMPTY, indent)
                        .append(field.getName())
                        .append(COLON)
                        .append(LBRACE)
                        .append(NEWLINE);
                recurse(sb, child, indent + indentSize, seen);
                sb.repeat(EMPTY, indent)
                        .append(RBRACE)
                        .append(NEWLINE);
            }
        }
    }



    public boolean isPrimitive(Object o){
        if (o == null) return true;
        else return o.getClass() == Integer.class
                || o.getClass() == Byte.class
                || o.getClass() == Short.class
                || o.getClass() == Long.class
                || o.getClass() == Double.class
                || o.getClass() == Float.class
                || o.getClass() == Character.class
                || o.getClass() == Boolean.class;
    }

    public boolean isEnum(Object o){
        return o instanceof Enum<?>;
    }

    public boolean isString(Object o){
        return o.getClass() == String.class;
    }

    public boolean isArray(Object o){
        return o.getClass().isArray();
    }

    public boolean isCollection(Object o){
        return o instanceof Collection<?> || o instanceof Map<?,?>;
    }

    public Object getChild(Field field, Object root){
        try {
            return field.get(root);
        } catch (IllegalAccessException e) {
            return null;
        }

    }
}


