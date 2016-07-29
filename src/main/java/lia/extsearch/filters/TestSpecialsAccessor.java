package lia.extsearch.filters;

// From chapter 6

public class TestSpecialsAccessor implements SpecialsAccessor {
    private String[] isbns;

    public TestSpecialsAccessor(String[] isbns) {
        this.isbns = isbns;
    }

    public String[] isbns() {
        return isbns;
    }

}
