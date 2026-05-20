export default function StatsCard({ icon, label, value, color = 'primary' }) {
  const colorClasses = {
    primary: 'stats-primary',
    success: 'stats-success',
    warning: 'stats-warning',
    info: 'stats-info'
  };

  return (
    <div className={`stat-card ${colorClasses[color]}`}>
      <div className="stat-icon">{icon}</div>
      <div className="stat-content">
        <div className="stat-value">{value}</div>
        <div className="stat-label">{label}</div>
      </div>
    </div>
  );
}