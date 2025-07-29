import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Header from './components/Header';
import HomePage from './pages/HomePage';
import TeamsPage from './pages/TeamsPage';
import PlayersPage from './pages/PlayersPage';
import TransfersPage from './pages/TransfersPage';

function App() {
  return (
    <div className="app">
      <Header />
      <div className="container">
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/teams" element={<TeamsPage />} />
          <Route path="/players" element={<PlayersPage />} />
          <Route path="/transfers" element={<TransfersPage />} />
        </Routes>
      </div>
    </div>
  );
}

export default App;
