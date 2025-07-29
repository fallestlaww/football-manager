import React, { useState, useEffect } from 'react';
import { getPlayers, createPlayer, updatePlayer, deletePlayer } from '../services/api';
import PlayerCard from '../components/PlayerCard';
import PlayerForm from '../components/PlayerForm';

const PlayersPage = () => {
  const [players, setPlayers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [editingPlayer, setEditingPlayer] = useState(null);

  useEffect(() => {
    fetchPlayers();
  }, []);

  const fetchPlayers = async () => {
    try {
      setLoading(true);
      const response = await getPlayers();
      setPlayers(response.data);
      setError(null);
    } catch (err) {
      setError('Error loading players');
    } finally {
      setLoading(false);
    }
  };

  const handleCreatePlayer = async (playerData) => {
    try {
      await createPlayer(playerData);
      setShowForm(false);
      fetchPlayers();
    } catch (err) {
      setError('Error creating player');
    }
  };

  const handleUpdatePlayer = async (playerData) => {
    try {
      await updatePlayer(editingPlayer.id, playerData);
      setShowForm(false);
      setEditingPlayer(null);
      fetchPlayers();
    } catch (err) {
      setError('Error updating player');
    }
  };

  const handleDeletePlayer = async (id) => {
    if (window.confirm('Are you sure you want to delete this player?')) {
      try {
        await deletePlayer(id);
        fetchPlayers();
      } catch (err) {
        setError('Error deleting player');
      }
    }
  };

  if (loading) return <div>Loading...</div>;

  return (
      <div>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
          <h2 className="card-title">Players</h2>
          <button
              className="btn btn-primary"
              onClick={() => {
                setEditingPlayer(null);
                setShowForm(true);
                setError(null);
              }}
          >
            Create Player
          </button>
        </div>

        {error && <div className="alert alert-error">{error}</div>}

        {showForm && (
            <div className="card">
              <h3>{editingPlayer ? 'Edit Player' : 'Add New Player'}</h3>
              <PlayerForm
                  player={editingPlayer}
                  onSubmit={editingPlayer ? handleUpdatePlayer : handleCreatePlayer}
                  onCancel={() => {
                    setShowForm(false);
                    setEditingPlayer(null);
                    setError(null);
                  }}
              />
            </div>
        )}

        <div className="list">
          {players.map(player => (
              <PlayerCard
                  key={player.id}
                  player={player}
                  onEdit={(player) => {
                    setEditingPlayer(player);
                    setShowForm(true);
                    setError(null);
                  }}
                  onDelete={handleDeletePlayer}
              />
          ))}
        </div>
      </div>
  );
};

export default PlayersPage;
