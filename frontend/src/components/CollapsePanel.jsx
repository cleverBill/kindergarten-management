import { useState } from 'react';

export default function CollapsePanel({ title, defaultOpen = true, children, className = '' }) {
  const [isOpen, setIsOpen] = useState(defaultOpen);

  return (
    <div className={`collapse-panel ${className}`}>
      <div className="collapse-header" onClick={() => setIsOpen(!isOpen)}>
        <span className="collapse-title">{title}</span>
        <button className="collapse-toggle">
          {isOpen ? '▼' : '▶'}
        </button>
      </div>
      <div className={`collapse-content ${isOpen ? 'open' : ''}`}>
        {children}
      </div>
    </div>
  );
}