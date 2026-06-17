import { useParams } from "react-router-dom";
import Person from "./Person";

// Страница анализов конкретного члена семьи — переиспользует Person по personId.
export default function FamilyMember() {
  const { personId } = useParams<{ personId: string }>();
  const id = Number(personId);
  if (!Number.isFinite(id)) return null;
  return <Person key={id} personId={id} />;
}
