import React, { useState, useEffect } from 'react';
import { getPlayers, getTeams } from '../services/api';

const TransferModal = ({ onClose, onTransfer }) => {
  const [players, setPlayers] = useState([]);
  const [teams, setTeams] = useState([]);
  const [selectedPlayer, setSelectedPlayer] = useState('');
  const [selectedTeam, setSelectedTeam] = useState('');
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetchPlayersAndTeams();
  }, []);

  const fetchPlayersAndTeams = async () => {
    try {
      setLoading(true);
      const [playersResponse, teamsResponse] = await Promise.all([
        getPlayers(),
        getTeams()
      ]);
      setPlayers(playersResponse.data);
      setTeams(teamsResponse.data);
      setError(null);
    } catch (err) {
      let errorMessage = 'Loading error';

      if (err.response) {
        errorMessage = `Error ${err.response.status}: ${err.response.statusText}`;
        if (err.response.data && err.response.data.message) {
          errorMessage += ` - ${err.response.data.message}`;
        }
      } else if (err.request) {
        errorMessage = 'No response received from server';
      } else {
        errorMessage = err.message;
      }

      console.error(errorMessage);
      setError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (selectedPlayer && selectedTeam) {
      setError(null);
      try {
        await onTransfer(selectedPlayer, selectedTeam);
      } catch (err) {
        let errorMessage = 'Transferring error';

        if (err.response) {
          errorMessage = `Error ${err.response.status}: ${err.response.statusText}`;
          if (err.response.data && err.response.data.message) {
            errorMessage += ` - ${err.response.data.message}`;
          }
        } else if (err.request) {
          errorMessage = 'No response received from server';
        } else {
          errorMessage = err.message;
        }

        setError(errorMessage);
      }
    }
  };

  return (
    <div className="modal">
      <div className="modal-content">
        <div className="modal-header">
          <h2>Transfer player</h2>
          <button className="close-button" onClick={onClose}>&times;</button>
        </div>

        {loading && <div className="alert alert-info">Loading</div>}

        {error && (
          <div className="alert alert-error">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="player">Player:</label>
            <select
                id="player"
                value={selectedPlayer}
                onChange={(e) => setSelectedPlayer(e.target.value)}
                required
                disabled={loading}
            >
              <option value="">Choose team</option>
              {players.map(player => (
                  <option key={player.id} value={player.id}>
                    {player.first_name} {player.last_name} ({player.team_name})
                  </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="team">New team:</label>
            <select
                id="team"
                value={selectedTeam}
                onChange={(e) => setSelectedTeam(e.target.value)}
                required
                disabled={loading}
            >
              <option value="">Choose team</option>
              {teams.map(team => (
                  <option key={team.id} value={team.id}>{team.name}</option>
              ))}
            </select>
          </div>

          <button type="submit" className="btn btn-success" disabled={loading}>
            {loading ? 'Loading...' : 'Make transfer'}
          </button>
          <button type="button" className="btn btn-danger" onClick={onClose} disabled={loading}>
            Cancel
          </button>
        </form>
      </div>
    </div>
  );
};

export default TransferModal;
