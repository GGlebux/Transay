export const permissions = {
  ADMIN: [
    "users.manage",
    "transcripts.crud",
    "indicators.crud",
    "reasons.crud",
    "people.crud",
    "units.crud",
  ],
  EDITOR: [
    "transcripts.crud",
    "indicators.crud",
    "reasons.crud",
    "units.crud",
  ],
  USER: [
    "transcripts.read",
    "transcripts.update",
    "indicators.read",
    "units.read",
    "units.update",
    "reasons.own",
    "measurements.crud",
  ],
};