import NamedCrud from "../../components/admin/NamedCrud";
import { unitsApi } from "../../api/unitsApi";

export default function Units() {
  return <NamedCrud title="Единицы измерения" api={unitsApi} placeholder="Новая единица (г/л, фл, %...)" />;
}
