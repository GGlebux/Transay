// Типы, точно соответствующие контрактам бэкенда (проверено живыми запросами).

export type Gender = "MALE" | "FEMALE";
export type IndicatorGender = "MALE" | "FEMALE" | "BOTH";
export type Condition = "BASE" | "GRAVID" | "MENSES";
export type ReferentStatus = "OK" | "RAISE" | "FALL";
export type CustomerRole = "USER" | "EDITOR" | "ADMIN";
export type CustomerStatus = "PENDING" | "ACTIVE";

export type Reason = { id: number; name: string };
export type Unit = { id: number; name: string };

export type AgeRange = { years: number; months: number; days: number };

export type Customer = {
  id: number;
  email: string;
  status: CustomerStatus;
  role: CustomerRole;
  createdAt: string;
  personId: number | null;
  verified: boolean;
};

// GET /people, /people/all — dateOfBirth приходит в ISO (yyyy-MM-dd)
export type Person = {
  id: number;
  name: string;
  gender: Gender;
  dateOfBirth: string;
  condition: Condition;
  excludedReasons: Reason[];
};

// POST/PATCH /people — dateOfBirth ОТПРАВЛЯЕМ как dd.MM.yyyy (@JsonFormat на бэке)
export type PersonPayload = {
  name: string;
  gender: Gender;
  dateOfBirth: string;
  condition: Condition;
};

export type Indicator = {
  id: number;
  engName: string;
  rusName: string;
  gender: IndicatorGender;
  condition: Condition;
  minAge: AgeRange;
  maxAge: AgeRange;
  minValue: number;
  maxValue: number;
  units: string | null;
};
export type IndicatorPayload = Omit<Indicator, "id">;

export type SimpleIndicator = { name: string; units: string[] };
export type IndicatorGroup = {
  id: number;
  groupName: string;
  indicators: SimpleIndicator[];
};
export type IndicatorGroupPayload = {
  groupName: string;
  indicators: SimpleIndicator[];
};

export type Transcript = {
  id: number;
  name: string;
  gender: IndicatorGender;
  falls: Reason[];
  raises: Reason[];
};
export type TranscriptPayload = {
  name: string;
  gender: IndicatorGender; // ModelMapper -> Enum.valueOf, нужен UPPERCASE
  fallsIds: number[];
  raisesIds: number[];
};

// Сводная таблица: GET /people/measures
export type Measure = {
  id: number;
  minValue: number;
  currentValue: number;
  maxValue: number;
  regDate: string; // ISO
  units: string;
  status: ReferentStatus; // UPPERCASE
  reasons: Reason[];
};
export type TableMeta = { indicatorName: string; measures: Measure[] };
export type SummaryGroup = { groupName: string; dates: string[]; metas: TableMeta[] };

// POST/PATCH /people/measures — regDate ОТПРАВЛЯЕМ как dd.MM.yyyy
export type MeasurePayload = {
  name: string;
  currentValue: number;
  units: string;
  regDate: string;
};

// GET /people/measures/decrypt?date=ISO — объект, ключ = имя причины
export type DecryptItem = {
  reasonId: number;
  matchesCount: number;
  percentage: number;
  indicators: string[];
};
export type DecryptResponse = Record<string, DecryptItem>;
