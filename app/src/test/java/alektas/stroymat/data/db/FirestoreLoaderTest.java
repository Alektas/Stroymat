package alektas.stroymat.data.db;

import org.junit.Test;

import static org.junit.Assert.*;

public class FirestoreLoaderTest {

    @Test
    public void floatFrom_objectLong_isCorrect() {
        Object o = new Long(1L);

        float f = new FirestoreLoader().floatFrom(o);
        assertEquals(1.0f, f, 0.0f);
    }


    @Test
    public void floatFrom_objectDouble_isCorrect() {
        Object o = new Double(1.0d);

        float f = new FirestoreLoader().floatFrom(o);
        assertEquals(1.0d, f, 0.0d);
    }

    @Test
    public void intFrom_objectLong_isCorrect() {
        Object o = new Long(1L);

        int f = new FirestoreLoader().intFrom(o);
        assertEquals(1, f);
    }

    @Test
    public void intFrom_objectDouble_isCorrect() {
        Object o = new Double(1.0d);

        int f = new FirestoreLoader().intFrom(o);
        assertEquals(1, f);
    }


    @Test
    public void intFrom_objectEmpty_isZero() {
        Object o = new Object();

        int f = new FirestoreLoader().intFrom(o);
        assertEquals(0, f);
    }

    @Test
    public void doubleFrom_objectEmpty_isZero() {
        Object o = new Object();

        float f = new FirestoreLoader().floatFrom(o);
        assertEquals(0.0f, f, 0.0f);
    }

    @Test
    public void intFrom_objectNull_isZero() {
        Object o = null;

        int f = new FirestoreLoader().intFrom(o);
        assertEquals(0, f);
    }

    @Test
    public void doubleFrom_objectNull_isZero() {
        Object o = null;

        float f = new FirestoreLoader().floatFrom(o);
        assertEquals(0.0f, f, 0.0f);
    }
}