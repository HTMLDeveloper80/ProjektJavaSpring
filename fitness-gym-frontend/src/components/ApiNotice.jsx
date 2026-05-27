export function ApiNotice({ state }) {
  return <p className={`apiNotice ${state.status}`}>{state.message}</p>;
}
