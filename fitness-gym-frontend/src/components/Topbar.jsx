import { Bell, Dumbbell, LogIn, Search, UserPlus, UserRound } from 'lucide-react';

export function Topbar({
  navItems,
  activeView,
  onViewChange,
  authUser,
  onLogin,
  onRegister,
  onLogout,
  onSearch,
  onNotifications,
  onClientPanel
}) {
  return (
    <header className="topbar">
      <button className="brand" type="button" onClick={() => onViewChange('dashboard')}>
        <span className="logo"><Dumbbell size={24} /></span>
        <span>FitCore</span>
      </button>

      <nav aria-label="Główna nawigacja">
        {navItems.map(({ id, label, icon: Icon }) => (
          <button
            key={id}
            type="button"
            onClick={() => onViewChange(id)}
            className={activeView === id ? 'active' : ''}
            title={label}
          >
            <Icon size={17} />
            <span>{label}</span>
          </button>
        ))}
      </nav>

      <div className="actions">
        <button className="icon" type="button" onClick={onSearch} aria-label="Szukaj">
          <Search size={19} />
        </button>
        <button className="icon" type="button" onClick={onNotifications} aria-label="Powiadomienia">
          <Bell size={19} />
        </button>

        {authUser ? (
          <>
            <button className="login" type="button" onClick={onClientPanel}>
              <UserRound size={18} />
              {authUser.name}
            </button>
            <button className="outline" type="button" onClick={onLogout}>Wyloguj</button>
          </>
        ) : (
          <>
            <button className="outline" type="button" onClick={onLogin}>
              <LogIn size={16} />
              Logowanie
            </button>
            <button className="login" type="button" onClick={onRegister}>
              <UserPlus size={16} />
              Rejestracja
            </button>
          </>
        )}
      </div>
    </header>
  );
}
