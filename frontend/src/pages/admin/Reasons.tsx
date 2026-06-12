import NamedCrud from "../../components/admin/NamedCrud";
import { reasonsApi } from "../../api/reasonsApi";

export default function Reasons() {
  return <NamedCrud title="Причины (повышения / понижения)" api={reasonsApi} placeholder="Новая причина" />;
}
