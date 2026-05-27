import { useState } from 'react';
import { LogIn, UserPlus } from 'lucide-react';
import { Modal } from './Modal.jsx';

export function AuthModal({ mode, onClose, onModeChange, onSubmit }) {
  const [form, setForm] = useState({
    name: '',
    email: '',
    password: ''
  });
  const isRegister = mode === 'register';

  function updateField(field, value) {
    setForm((current) => ({ ...current, [field]: value }));
  }

  function submit(event) {
    event.preventDefault();
    onSubmit(form);
  }

  return (
    <Modal
      title={isRegister ? 'Utwórz konto' : 'Zaloguj się'}
      subtitle={isRegister ? 'Wpisz swoje dane. Konto zostanie zapisane przez Spring API.' : 'Zaloguj się adresem, który wcześniej zarejestrowałeś.'}
      onClose={onClose}
    >
      <div className="segmented">
        <button className={!isRegister ? 'active' : ''} type="button" onClick={() => onModeChange('login')}>
          <LogIn size={16} /> Logowanie
        </button>
        <button className={isRegister ? 'active' : ''} type="button" onClick={() => onModeChange('register')}>
          <UserPlus size={16} /> Rejestracja
        </button>
      </div>
      <form className="authForm" onSubmit={submit}>
        {isRegister && (
          <label>
            Imię
            <input value={form.name} onChange={(event) => updateField('name', event.target.value)} placeholder="np. Jan" required minLength={2} />
          </label>
        )}
        <label>
          E-mail
          <input type="email" value={form.email} onChange={(event) => updateField('email', event.target.value)} placeholder="twoj@email.pl" required />
        </label>
        <label>
          Hasło
          <input type="password" value={form.password} onChange={(event) => updateField('password', event.target.value)} placeholder="minimum 6 znaków" required minLength={6} />
        </label>
        <button className="primary authSubmit" type="submit">
          {isRegister ? 'Załóż konto' : 'Wejdź do panelu'}
        </button>
      </form>
    </Modal>
  );
}
