CREATE TABLE "user" (
    "id" UUID NOT NULL,
    "email" TEXT NOT NULL,
    "password" TEXT NOT NULL,
    "first_name" TEXT NOT NULL,
    "last_name" TEXT NOT NULL,
    "created_at" TIMESTAMP NOT NULL,
    "updated_at" TIMESTAMP
);

CREATE TABLE "session" (
    "id" UUID NOT NULL,
    "token" TEXT NOT NULL,
    "user_id" UUID NOT NULL,
    "created_at" TIMESTAMP NOT NULL,
    "updated_at" TIMESTAMP
);

CREATE TABLE "activity" (
    "id" UUID NOT NULL,
    "name" TEXT NOT NULL,
    "description" TEXT,
    "distance" FLOAT,
    "elapsed_time" INT,
    "user_id" UUID NOT NULL,
    "created_at" TIMESTAMP NOT NULL,
    "updated_at" TIMESTAMP
);

ALTER TABLE "user"
    ADD CONSTRAINT "pk_user" PRIMARY KEY ("id"),
    ADD CONSTRAINT "uq_email" UNIQUE ("email");

ALTER TABLE "session"
    ADD CONSTRAINT "pk_session" PRIMARY KEY ("id"),
    ADD CONSTRAINT "uq_token" UNIQUE ("token"),
    ADD CONSTRAINT "fk_session_user" FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "activity"
    ADD CONSTRAINT "pk_activity" PRIMARY KEY ("id"),
    ADD CONSTRAINT "fk_activity_user" FOREIGN KEY ("user_id") REFERENCES "user" ("id");