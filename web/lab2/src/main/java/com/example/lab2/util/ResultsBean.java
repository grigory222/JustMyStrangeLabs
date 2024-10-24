package com.example.lab2.util;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultsBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Result> results;

    public ResultsBean() {
        this.results = new ArrayList<>();
    }

    public void addResult(Result result) {
        this.results.add(result);
    }

    public List<Result> getResults() {
        return results;
    }
    @Getter
    @Setter
    public static class Result implements Serializable {
        private static final long serialVersionUID = 2L;
        private String x;
        private String y;
        private String r;
        private long executionTime;
        private LocalTime time;
        private boolean isInside;

        public Result(String x, String y, String r, long executionTime, LocalTime time, boolean isInside) {
            this.x = x;
            this.y = y;
            this.r = r;
            this.isInside = isInside;
            this.time = time;
            this.executionTime = executionTime;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Result result = (Result) o;
            return isInside == result.isInside &&
                    x.equals(result.x) &&
                    y.equals(result.y) &&
                    r.equals(result.r);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, r, isInside);
        }

        @Override
        public String toString() {
            return "Result{" +
                    "x='" + x + '\'' +
                    ", y='" + y + '\'' +
                    ", r='" + r + '\'' +
                    ", isInside=" + isInside +
                    '}';
        }
    }
}