include(
    ":gemini-core",
    ":gemini-core-test",
    ":gemini-binder",
    ":sample"
)

if (startParameter.projectProperties.containsKey("check_publication")) {
    include(":check-publication")
}
