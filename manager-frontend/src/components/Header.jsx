
import React from 'react';
import { Link } from 'react-router-dom';

const Header = () => {
  return (
      <header className="header">
        <div className="header-content">
          <h1>Football Manager</h1>
          <nav>
            <ul className="nav-links">
              <li><Link to="/">Home</Link></li>
              <li><Link to="/teams">Teams</Link></li>
              <li><Link to="/players">Players</Link></li>
              <li><Link to="/transfers">Transfers</Link></li>
            </ul>
          </nav>
        </div>
      </header>
  );
};

export default Header;