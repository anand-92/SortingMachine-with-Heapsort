import static org.junit.Assert.assertEquals;

import java.util.Comparator;

import org.junit.Test;

import components.sortingmachine.SortingMachine;

/**
 * JUnit test fixture for {@code SortingMachine<String>}'s constructor and
 * kernel methods.
 *
 * @author Nik Anand and Hudson Arledge
 *
 */
public abstract class SortingMachineTest {

    /**
     * Invokes the appropriate {@code SortingMachine} constructor for the
     * implementation under test and returns the result.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @return the new {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures constructorTest = (true, order, {})
     */
    protected abstract SortingMachine<String> constructorTest(
            Comparator<String> order);

    /**
     * Invokes the appropriate {@code SortingMachine} constructor for the
     * reference implementation and returns the result.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @return the new {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures constructorRef = (true, order, {})
     */
    protected abstract SortingMachine<String> constructorRef(
            Comparator<String> order);

    /**
     *
     * Creates and returns a {@code SortingMachine<String>} of the
     * implementation under test type with the given entries and mode.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @param insertionMode
     *            flag indicating the machine mode
     * @param args
     *            the entries for the {@code SortingMachine}
     * @return the constructed {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures <pre>
     * createFromArgsTest = (insertionMode, order, [multiset of entries in args])
     * </pre>
     */
    private SortingMachine<String> createFromArgsTest(Comparator<String> order,
            boolean insertionMode, String... args) {
        SortingMachine<String> sm = this.constructorTest(order);
        for (int i = 0; i < args.length; i++) {
            sm.add(args[i]);
        }
        if (!insertionMode) {
            sm.changeToExtractionMode();
        }
        return sm;
    }

    /**
     *
     * Creates and returns a {@code SortingMachine<String>} of the reference
     * implementation type with the given entries and mode.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @param insertionMode
     *            flag indicating the machine mode
     * @param args
     *            the entries for the {@code SortingMachine}
     * @return the constructed {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures <pre>
     * createFromArgsRef = (insertionMode, order, [multiset of entries in args])
     * </pre>
     */
    private SortingMachine<String> createFromArgsRef(Comparator<String> order,
            boolean insertionMode, String... args) {
        SortingMachine<String> sm = this.constructorRef(order);
        for (int i = 0; i < args.length; i++) {
            sm.add(args[i]);
        }
        if (!insertionMode) {
            sm.changeToExtractionMode();
        }
        return sm;
    }

    /**
     * Comparator<String> implementation to be used in all test cases. Compare
     * {@code String}s in lexicographic order.
     */
    private static class StringLT implements Comparator<String> {

        @Override
        public int compare(String s1, String s2) {
            return s1.compareToIgnoreCase(s2);
        }

    }

    /**
     * Comparator instance to be used in all test cases.
     */
    private static final StringLT ORDER = new StringLT();

    /*
     * Sample test cases.
     */

    @Test
    public final void testConstructor() {
        SortingMachine<String> m = this.constructorTest(ORDER);
        SortingMachine<String> mExpected = this.constructorRef(ORDER);
        assertEquals(mExpected, m);
    }

    @Test
    public final void testAddEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "green");
        m.add("green");
        assertEquals(mExpected, m);
    }

    // TODO - add test cases for add, changeToExtractionMode, removeFirst,
    // isInInsertionMode, order, and size

    @Test
    public final void testAddNonEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true,
                "green");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "green", "red");
        m.add("red");
        assertEquals(mExpected, m);
    }

    @Test
    public final void testChangeToExtractionModeEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false);
        m.changeToExtractionMode();
        assertEquals(mExpected, m);
    }

    @Test
    public final void testChangeToExtractionModeNonEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true,
                "green");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "green");
        m.changeToExtractionMode();
        assertEquals(mExpected, m);
    }

    @Test
    public final void testRemoveFirstToEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false,
                "green");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false);

        String removed = m.removeFirst();
        assertEquals(removed, "green");
        assertEquals(mExpected, m);
    }

    @Test
    public final void testRemoveFirstMultipleEntries() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false, "blue",
                "green", "red");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "green", "red");

        String removed = m.removeFirst();
        assertEquals(removed, "blue");
        assertEquals(mExpected, m);
    }

    @Test
    public final void testisNotInInsertionMode() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false, "blue",
                "green");
        SortingMachine<String> mExpected = this.createFromArgsTest(ORDER, false,
                "blue", "green");
        boolean modeExpected = false;

        boolean modeM = m.isInInsertionMode();
        assertEquals(modeM, modeExpected);
        assertEquals(m, mExpected);
    }

    @Test
    public final void testIsInInsertionMode() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "blue",
                "green");
        SortingMachine<String> mExpected = this.createFromArgsTest(ORDER, true,
                "blue", "green");
        boolean modeExpected = true;

        boolean modeM = m.isInInsertionMode();
        assertEquals(modeM, modeExpected);
        assertEquals(m, mExpected);
    }

    @Test
    public final void testisNotInInsertionModeEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false);
        SortingMachine<String> mExpected = this.createFromArgsTest(ORDER,
                false);
        boolean modeExpected = false;

        boolean modeM = m.isInInsertionMode();
        assertEquals(modeM, modeExpected);
        assertEquals(m, mExpected);
    }

    @Test
    public final void testIsInInsertionModeEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsTest(ORDER, true);
        boolean modeExpected = true;

        boolean modeM = m.isInInsertionMode();
        assertEquals(modeM, modeExpected);
        assertEquals(m, mExpected);
    }

    @Test
    public final void testOrder() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsTest(ORDER, true);
        assertEquals(m.order(), ORDER);
        assertEquals(m, mExpected);
    }

    @Test
    public final void testSizeInsertionModeEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsTest(ORDER, true);
        assertEquals(m.size(), 0);
        assertEquals(m, mExpected);
    }

    @Test
    public final void testSizeInsertionModeNonEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "blue",
                "green");
        SortingMachine<String> mExpected = this.createFromArgsTest(ORDER, true,
                "blue", "green");
        assertEquals(m.size(), 2);
        assertEquals(m, mExpected);
    }

    @Test
    public final void testSizeExtractionModeEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false);
        SortingMachine<String> mExpected = this.createFromArgsTest(ORDER,
                false);
        assertEquals(m.size(), 0);
        assertEquals(m, mExpected);
    }

    @Test
    public final void testSizeExtractionModeNonEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false, "blue",
                "green");
        SortingMachine<String> mExpected = this.createFromArgsTest(ORDER, false,
                "blue", "green");
        assertEquals(m, mExpected);
        assertEquals(m.size(), 2);
    }
}
