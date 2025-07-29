import React from 'react';
import { Link } from 'react-router-dom';

const HomePage = () => {
  return (
    <div>
      <div className="card">
        <h1 className="card-title">Welcome to Football Manager</h1>
        <p>This is an application for managing football teams and players.</p>
        <p>You can:</p>
        <ul>
          <li>Create and edit teams</li>
          <li>Add and manage players</li>
          <li>Transfer players between teams</li>
        </ul>
      </div>

      <div style={{ display: 'flex', gap: '20px', flexWrap: 'wrap' }}>
        <Link to="/teams" className="btn btn-primary">View Teams</Link>
        <Link to="/players" className="btn btn-primary">View Players</Link>
        <Link to="/transfers" className="btn btn-warning">Make Transfer</Link>
      </div>
    </div>
  );
};

export default HomePage;
