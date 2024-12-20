CREATE TABLE "person"(
    "id" INTEGER NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    "gender" VARCHAR(255) NOT NULL,
    "date_of_birth" DATE NOT NULL,
    "is_gravid" BOOLEAN NOT NULL DEFAULT '0'
);
ALTER TABLE
    "person" ADD PRIMARY KEY("id");
CREATE TABLE "indicator"(
    "id" INTEGER NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    "gender" VARCHAR(255) NOT NULL,
    "is_gravid" BOOLEAN NOT NULL DEFAULT '0',
    "min_age" INTEGER NOT NULL,
    "max_age" INTEGER NOT NULL,
    "min_value" FLOAT(53) NOT NULL,
    "max_value" FLOAT(53) NOT NULL
);
ALTER TABLE
    "indicator" ADD PRIMARY KEY("id");
CREATE TABLE "person_info"(
    "id" INTEGER NOT NULL,
    "person_id" INTEGER NOT NULL,
    "indicator_id" INTEGER NOT NULL,
    "referent_id" INTEGER NOT NULL
);
ALTER TABLE
    "person_info" ADD PRIMARY KEY("id");
CREATE TABLE "transcript"(
    "id" INTEGER NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    "fall" jsonb NOT NULL,
    "raise" jsonb NOT NULL
);
ALTER TABLE
    "transcript" ADD PRIMARY KEY("id");
CREATE TABLE "referent"(
    "id" INTEGER NOT NULL,
    "current_value" FLOAT(53) NOT NULL,
    "reg_date" DATE NOT NULL,
    "units" VARCHAR(255) NOT NULL,
    "status" VARCHAR(255) NOT NULL,
    "transcript_id" INTEGER NOT NULL
);
ALTER TABLE
    "referent" ADD PRIMARY KEY("id");
CREATE TABLE "excluded_reasons"(
    "id" INTEGER NOT NULL,
    "person_id" INTEGER NOT NULL,
    "reason" VARCHAR(255) NOT NULL
);
ALTER TABLE
    "excluded_reasons" ADD PRIMARY KEY("id");
ALTER TABLE
    "excluded_reasons" ADD CONSTRAINT "excluded_reasons_reason_unique" UNIQUE("reason");
ALTER TABLE
    "person_info" ADD CONSTRAINT "person_info_indicator_id_foreign" FOREIGN KEY("indicator_id") REFERENCES "indicator"("id");
ALTER TABLE
    "referent" ADD CONSTRAINT "referent_transcript_id_foreign" FOREIGN KEY("transcript_id") REFERENCES "transcript"("id");
ALTER TABLE
    "person_info" ADD CONSTRAINT "person_info_person_id_foreign" FOREIGN KEY("person_id") REFERENCES "person"("id");
ALTER TABLE
    "person_info" ADD CONSTRAINT "person_info_referent_id_foreign" FOREIGN KEY("referent_id") REFERENCES "referent"("id");
ALTER TABLE
    "excluded_reasons" ADD CONSTRAINT "excluded_reasons_person_id_foreign" FOREIGN KEY("person_id") REFERENCES "person"("id");