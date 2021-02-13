include(":gemini-lint")
include(
    ":gemini-core",
    ":gemini-core-test",
    ":gemini-store-keeper",
    ":gemini-binder",
    ":sample"
)

if (startParameter.projectProperties.containsKey("check_publication")) {
    include(":check-publication")
}
