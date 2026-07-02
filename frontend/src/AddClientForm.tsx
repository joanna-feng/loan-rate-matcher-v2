import { useState, type FormEvent } from 'react'
import { API_BASE_URL } from './config'

interface AddClientFormProps {
  onClientAdded: () => void
}

function AddClientForm({ onClientAdded }: AddClientFormProps) {
  const [name, setName] = useState('')
  const [creditScore, setCreditScore] = useState('')
  const [error, setError] = useState<string | null>(null)

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    setError(null)

    try {
      const response = await fetch(`${API_BASE_URL}/api/clients`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, creditScore: Number(creditScore) }),
      })

      if (response.status === 409) {
        setError(`A client named "${name}" already exists.`)
        return
      }
      if (!response.ok) {
        throw new Error(`Request failed with status ${response.status}`)
      }

      setName('')
      setCreditScore('')
      onClientAdded()
    } catch {
      setError('Could not add client. Is the backend running?')
    }
  }

  return (
    <form onSubmit={handleSubmit}>
      <label htmlFor="newClientName">Name</label>
      <input
        id="newClientName"
        value={name}
        onChange={(event) => setName(event.target.value)}
        required
      />
      <label htmlFor="newClientCreditScore">Credit Score</label>
      <input
        id="newClientCreditScore"
        type="number"
        value={creditScore}
        onChange={(event) => setCreditScore(event.target.value)}
        required
      />
      <button type="submit">Add Client</button>
      {error && <p className="error">{error}</p>}
    </form>
  )
}

export default AddClientForm
