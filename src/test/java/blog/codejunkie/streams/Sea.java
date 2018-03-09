package blog.codejunkie.streams;

import java.util.Objects;

public class Sea {
    private String region;
    private Integer area;
    private String name;

    public Sea(String name, String region, Integer area) {
        this.name = name;
        this.area = area;
        this.region = region;
    }

    public String getName() {
        return name;
    }

    public Integer getArea() {
        return area;
    }

    public String getRegion() {
        return region;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sea sea = (Sea) o;
        return Objects.equals(region, sea.region) &&
                Objects.equals(area, sea.area) &&
                Objects.equals(name, sea.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, area, name);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Sea{");
        sb.append("region='").append(region).append('\'');
        sb.append(", area=").append(area);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
