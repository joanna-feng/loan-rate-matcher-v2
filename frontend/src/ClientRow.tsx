import { useState } from 'react'
import { Pencil, Trash2 } from 'lucide-react'
import type { Client } from './ClientList'
import { API_BASE_URL } from './config'

interface ClientRowProps {
  client: Client
  onUpdated: (updated: Client) => void
  onDeleted: (id: number) => void
}

function ClientRow({ client, onUpdated, onDeleted }: ClientRowProps) {
  const [creditScore, setCreditScore] = useState(String(client.creditScore))
  const [error, setError] = useState<string | null>(null)

  async function handleUpdate() {
    setError(null)
    try {
      const response = await fetch(`${API_BASE_URL}/api/clients/${client.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ creditScore: Number(creditScore) }),
      })
      if (!response.ok) {
        throw new Error(`Request failed with status ${response.status}`)
      }
      const updated: Client = await response.json()
      onUpdated(updated)
    } catch {
      setError('Could not update credit score.')
    }
  }

  async function handleDelete() {
    setError(null)
    try {
      const response = await fetch(`${API_BASE_URL}/api/clients/${client.id}`, {
        method: 'DELETE',
      })
      if (!response.ok) {
        throw new Error(`Request failed with status ${response.status}`)
      }
      onDeleted(client.id)
    } catch {
      setError('Could not delete client.')
    }
  }

  return (
    <>
      <tr>
        <td>{client.id}</td>
        <td>{client.name}</td>
        <td>
          <input
            type="number"
            value={creditScore}
            onChange={(event) => setCreditScore(event.target.value)}
          />
        </td>
        <td>{client.loanRate}%</td>
        <td className="row-actions">
          <button type="button" onClick={handleUpdate}>
            <Pencil size={14} strokeWidth={2.5} />
            Update
          </button>
          <button type="button" className="btn-delete" onClick={handleDelete}>
            <Trash2 size={14} strokeWidth={2.5} />
            Delete
          </button>
        </td>
      </tr>
      {error && (
        <tr>
          <td colSpan={5} className="error">
            {error}
          </td>
        </tr>
      )}
    </>
  )
}

export default ClientRow
