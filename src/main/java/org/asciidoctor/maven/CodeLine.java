package org.asciidoctor.maven;

public class CodeLine {
    private String code;

    private CodeLine(Builder builder) {
        this.code = builder.code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String code = "";

        public Builder tab(Integer n) {
            if (n == 0) {
                return this;
            }

            this.code = this.code + " ".repeat(4*n);
            return this;
        }

        public Builder tab() {
            this.code = this.code + " ".repeat(4*1);
            return this;
        }

        public Builder code(String code) {
            this.code = this.code + code;
            return this;
        }

        public Builder newLine(Integer n) {
            if (n == 0) {
                return this;
            }

            this.code = this.code + "\r\n".repeat(n);
            return this;
        }

        public Builder newLine() {
            this.code = this.code + "\r\n";
            return this;
        }

        public CodeLine build() {

            return new CodeLine(this);
        }
    }
}
